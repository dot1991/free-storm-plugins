package net.unethicalite.glassblower

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.coords.WorldPoint
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
import net.unethicalite.api.movement.pathfinder.model.BankLocation
import net.unethicalite.api.packets.WidgetPackets
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.api.utils.MessageUtils
import net.unethicalite.api.widgets.Dialog
import net.unethicalite.api.widgets.Production
import net.unethicalite.client.Static
import net.unethicalite.glassblower.util.*
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Glass Blower",
    description = "Automatic glass blower",
    tags = ["glass"]
)
class GlassBlowerPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: GlassBlowerConfig

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
    fun provideConfig(configManager: ConfigManager): GlassBlowerConfig {
        return configManager.getConfig(GlassBlowerConfig::class.java)
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
            MessageUtils.addMessage(getState().name)
            when(getState()){
                States.HANDLE_BREAK -> {
                    MessageUtils.addMessage("Attempting to break")
                    chinBreakHandler.startBreak(this@GlassBlowerPlugin)
                }
                States.HANDLE_BANK -> {
                    if (Bank.isOpen())
                    {
                        if (Inventory.contains{it.id != ItemID.GLASSBLOWING_PIPE && it.id != ItemID.MOLTEN_GLASS})
                        {
                            Bank.depositInventory()
                            return sleepDelay().toInt()
                        }
                        if (!Inventory.contains(ItemID.GLASSBLOWING_PIPE))
                        {
                            if (Bank.contains(ItemID.GLASSBLOWING_PIPE))
                            {
                                Bank.withdraw(ItemID.GLASSBLOWING_PIPE, 1, Bank.WithdrawMode.ITEM)
                            }
                            else
                            {
                                startPlugin = false
                                return -1
                            }
                            return sleepDelay().toInt()
                        }
                        if (!Inventory.contains(ItemID.MOLTEN_GLASS))
                        {
                            if (Bank.contains(ItemID.MOLTEN_GLASS))
                            {
                                Bank.withdraw(ItemID.MOLTEN_GLASS, 27, Bank.WithdrawMode.ITEM)

                            }
                            else
                            {
                                startPlugin = false
                                return -1
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
                States.BLOW_GLASS -> {
                    Dialog.close()
                    if (Production.isOpen())
                    {
                        Production.chooseOption(if(config.productType() == Product.HIGHEST_POSSIBLE && Product.getHighest() != null) Product.getHighest()!!.keyValue else config.productType().keyValue)
                        Time.sleep(5000)
                        Time.sleepUntil({!Inventory.contains(ItemID.MOLTEN_GLASS) || (Dialog.isOpen())}, {Players.getLocal().animation != -1}, 5000)
                    }
                    else
                    {
                        var glass: Item? = Inventory.getFirst(ItemID.MOLTEN_GLASS)
                        var pipe: Item? = Inventory.getFirst(ItemID.GLASSBLOWING_PIPE)

                        if (glass != null && pipe != null)
                        {
                            glass.useOn(pipe)
                            Time.sleepUntil({Production.isOpen()}, 2500)
                        }
                    }
                }
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
        if (!configButtonClicked.group.equals("GlassBlowerConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
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