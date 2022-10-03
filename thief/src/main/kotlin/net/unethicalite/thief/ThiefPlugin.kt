package net.unethicalite.thief

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.NPCs
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.items.Bank
import net.unethicalite.api.items.Inventory
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.api.utils.MessageUtils
import net.unethicalite.api.widgets.Dialog
import net.unethicalite.client.Static
import net.unethicalite.thief.util.Calculation
import net.unethicalite.thief.util.Functions
import net.unethicalite.thief.util.Log
import net.unethicalite.thief.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Thief",
    description = "Automatic thief",
    tags = ["thief"]
)
class ThiefPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: ThiefConfig

    @Inject
    lateinit var functions: Functions

    @Inject
    lateinit var calculation: Calculation

    @Inject
    lateinit var chinBreakHandler: ReflectBreakHandler

    var sleepLength: Long = -1

    private var startTime: Instant = Instant.now()

    private val runtime: Duration get() = Duration.between(startTime, Instant.now())

    var startPlugin: Boolean = false

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): ThiefConfig {
        return configManager.getConfig(ThiefConfig::class.java)
    }


    override fun startUp() {
        log.info("${this::class.simpleName} started at $startTime")
        chinBreakHandler.registerPlugin(this)
        reset()
    }

    override fun shutDown() {
        log.info("${this::class.simpleName} stopped at ${Instant.now()} with runtime $runtime")
        chinBreakHandler.unregisterPlugin(this)
        reset()
    }

    override fun loop(): Int {
        if (!startPlugin || chinBreakHandler.isBreakActive(this)) return 100

        with(functions) {
            if (config.enableDebugging()) MessageUtils.addMessage("State: " + getState().name)

            when(getState()){
                States.HANDLE_BREAK -> {
                    MessageUtils.addMessage("Attempting to break")
                    chinBreakHandler.startBreak(this@ThiefPlugin)
                }
                States.DROP -> {
                    if (config.stall().item == -1)
                    {
                        for (item in Inventory.getAll()){
                            item.interact("Drop")
                            Time.sleep(75)
                        }
                    }
                    else{
                        for (item in Inventory.getAll { it.id == config.stall().item })
                        {
                            item.interact("Drop")
                            Time.sleep(75)
                        }
                    }
                }
                States.STEAL -> {
                    var stall: TileObject? = TileObjects.getNearest { it.id == config.stall().normal && it.distanceTo(Players.getLocal()) <= 2}
                    if (Players.getLocal().isAnimating)
                        return -1
                    stall?.let {
                        it.interact(config.stall().action)
                    }
                    return -2
                }
                /*States.HANDLE_BANK -> {
                    if (Bank.isOpen())
                    {
                        if (!Inventory.contains { it.id == ItemID.JUG_OF_WINE } || Inventory.getCount(ItemID.JUG_OF_WINE) < 20)
                        {
                            return if (Bank.contains { it.id == ItemID.JUG_OF_WINE }) {
                                Bank.withdraw(ItemID.JUG_OF_WINE, 20 - Inventory.getCount(ItemID.JUG_OF_WINE), Bank.WithdrawMode.ITEM)
                                -3
                            } else {
                                startPlugin = false
                                -1
                            }
                        }
                        if (!Inventory.contains { it.id == ItemID.DODGY_NECKLACE } || Inventory.getCount(ItemID.DODGY_NECKLACE) < 4)
                        {
                            return if (Bank.contains { it.id == ItemID.DODGY_NECKLACE }) {
                                Bank.withdraw(ItemID.DODGY_NECKLACE, 4 - Inventory.getCount(ItemID.DODGY_NECKLACE), Bank.WithdrawMode.ITEM)
                                -3
                            } else {
                                startPlugin = false
                                -1
                            }
                        }

                    }
                    else
                    {
                        var banker: NPC? = NPCs.getNearest{it.hasAction("Bank")}
                        banker?.interact("Bank")
                        Time.sleepUntil({Bank.isOpen()}, 2500)
                    }
                }
                States.STEAL -> {
                    Dialog.close()
                    var knight: NPC? = NPCs.getNearest { it.id == config.npcID() && it.distanceTo(Players.getLocal()) <= 7}
                    knight?.interact("Pickpocket")
                    return -1
                }
                States.EAT_FOOD -> {
                    Inventory.getFirst { it.id == ItemID.JUG_OF_WINE }?.interact("Drink")
                    return -1
                }

                States.EQUIP_NECKLACE -> {
                    Inventory.getFirst { it.id == ItemID.DODGY_NECKLACE }?.interact("Wear")
                    return -1
                }

                States.DROP_JUG -> {
                    for (Item in Inventory.getAll { it.id == ItemID.JUG })
                    {
                        Item.interact("Drop")
                        Time.sleep(sleepDelay())
                    }
                }
                States.OPEN_POUCH -> {
                    Inventory.getFirst { "Coin pouch" in it.name }?.interact("Open-all")
                    return -1
                }
                 */
                States.UNKNOWN -> {
                    //MessageUtils.addMessage("Reached unknown")
                }

            }
            return sleepDelay().toInt()
        }
    }

    private fun reset() {
        sleepLength = -1
        startPlugin = false
    }

    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("ThiefConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
        if (configButtonClicked.key.equals("startHelper", ignoreCase = true)) {
            startPlugin = !startPlugin
            MessageUtils.addMessage("Plugin running: $startPlugin")
            if(startPlugin)
                chinBreakHandler.startPlugin(this)
            else
                chinBreakHandler.stopPlugin(this)
        }
    }

}