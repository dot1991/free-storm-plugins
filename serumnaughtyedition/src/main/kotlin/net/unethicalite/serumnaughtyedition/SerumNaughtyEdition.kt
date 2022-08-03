package net.unethicalite.serumnaughtyedition

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.coords.WorldPoint
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.runelite.client.plugins.questlist.QuestListPlugin
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.NPCs
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.items.Bank
import net.unethicalite.api.items.Inventory
import net.unethicalite.api.movement.Movement
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.api.utils.MessageUtils
import net.unethicalite.api.widgets.Production
import net.unethicalite.client.Static
import net.unethicalite.serumnaughtyedition.util.Calculation
import net.unethicalite.serumnaughtyedition.util.Functions
import net.unethicalite.serumnaughtyedition.util.Log
import net.unethicalite.serumnaughtyedition.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Serum (XXX Edition)",
    description = "Automatic Serum Maker",
    tags = ["serum"]
)
class SerumNaughtyEdition : LoopedPlugin() {

    @Inject
    lateinit var config: SerumNaughtyEditionConfig

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

    var deposited: Boolean = false

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): SerumNaughtyEditionConfig {
        return configManager.getConfig(SerumNaughtyEditionConfig::class.java)
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

            if(chinBreakHandler.shouldBreak(this@SerumNaughtyEdition)){
                MessageUtils.addMessage("Attempting to break")
                chinBreakHandler.startBreak(this@SerumNaughtyEdition)
            }
            if(Inventory.contains(if(config.makecompost()) ItemID.SULPHUROUS_FERTILISER else ItemID.SERUM_207_3) && !deposited){ //serum 3
                if(Bank.isOpen()){
                    Bank.depositInventory()
                    deposited = true
                    Time.sleep(100)
                }else{
                    var banker: NPC? = NPCs.getNearest { it.hasAction("Bank") }
                    if (banker != null) {
                        banker.interact("Bank")
                        Time.sleepUntil({ Bank.isOpen() }, 1500)
                        return 1
                    }
                }
            }

            if(!Inventory.contains(if(config.makecompost()) ItemID.COMPOST else ItemID.ASHES) || !Inventory.contains(if(config.makecompost()) ItemID.SALTPETRE else ItemID.TARROMIN_POTION_UNF)){ //if(config.makecompost()) ItemID.COMPOST else ItemID.ASHES
                if (Bank.isOpen())
                {

                    if (Bank.contains(if(config.makecompost()) ItemID.COMPOST else ItemID.ASHES) && !Inventory.contains(if(config.makecompost()) ItemID.COMPOST else ItemID.ASHES))
                    {
                        Bank.withdraw(if(config.makecompost()) ItemID.COMPOST else ItemID.ASHES, 14, Bank.WithdrawMode.ITEM)
                    }else{
                        startPlugin = false
                        return -1
                    }

                    if (Bank.contains(if(config.makecompost()) ItemID.SALTPETRE else ItemID.TARROMIN_POTION_UNF) && !Inventory.contains(if(config.makecompost()) ItemID.SALTPETRE else ItemID.TARROMIN_POTION_UNF)) //taromin
                    {
                        Bank.withdraw(if(config.makecompost()) ItemID.SALTPETRE else ItemID.TARROMIN_POTION_UNF, 14, Bank.WithdrawMode.ITEM)

                    }else{
                        startPlugin = false
                        return -1
                    }
                    Time.sleepUntil({Inventory.contains(if(config.makecompost()) ItemID.COMPOST else ItemID.ASHES) && Inventory.contains(if(config.makecompost()) ItemID.SALTPETRE else ItemID.TARROMIN_POTION_UNF)}, 1000)
                    Bank.close()
                }
                else
                {
                    var banker: NPC? = NPCs.getNearest { it.hasAction("Bank") }

                    if (banker != null)
                    {
                        banker.interact("Bank")
                        Time.sleepUntil({ Bank.isOpen() }, 1500)
                    }
                    return 10
                }
            }
            if(Bank.isOpen() && Inventory.contains(if(config.makecompost()) ItemID.COMPOST else ItemID.ASHES) && Inventory.contains(if(config.makecompost()) ItemID.SALTPETRE else ItemID.TARROMIN_POTION_UNF)){
                Bank.close()
            }

            deposited = false
            if(!Bank.isOpen()){
                for (i in 0..13)
                {
                    var item1: Item? = Inventory.getItem(i)
                    if(item1 != null){
                        item1.useOn(Inventory.getItem(i + 14))
                        Time.sleep(50)
                    }
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
        if (!configButtonClicked.group.equals("SerumNaughtyEditionConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
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