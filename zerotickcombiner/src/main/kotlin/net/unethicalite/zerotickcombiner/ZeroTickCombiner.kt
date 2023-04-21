package net.unethicalite.zerotickcombiner

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.storm.api.commons.Time
import net.storm.api.entities.NPCs
import net.storm.api.entities.Players
import net.storm.api.items.Bank
import net.storm.api.items.Inventory
import net.storm.api.plugins.LoopedPlugin
import net.storm.api.utils.MessageUtils
import net.storm.client.Static
import net.unethicalite.zerotickcombiner.util.Calculation
import net.unethicalite.zerotickcombiner.util.Functions
import net.unethicalite.zerotickcombiner.util.Log
import net.unethicalite.zerotickcombiner.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "0 Tick Combiner",
    description = "Automatic 0 Tick combiner",
    tags = ["serum", "compost"]
)
class ZeroTickCombiner : LoopedPlugin() {

    @Inject
    lateinit var config: ZeroTickCombinerConfig

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
    fun provideConfig(configManager: ConfigManager): ZeroTickCombinerConfig {
        return configManager.getConfig(ZeroTickCombinerConfig::class.java)
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

            if(chinBreakHandler.shouldBreak(this@ZeroTickCombiner)){
                MessageUtils.addMessage("Attempting to break")
                chinBreakHandler.startBreak(this@ZeroTickCombiner)
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
                    //Time.sleepUntil({Inventory.contains(if(config.makecompost()) ItemID.COMPOST else ItemID.ASHES) && Inventory.contains(if(config.makecompost()) ItemID.SALTPETRE else ItemID.TARROMIN_POTION_UNF)}, 1000)
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
                        Time.sleep(25)
                    }
                }
            }

            return 0
        }
    }

    private fun reset() {
        sleepLength = -1
        startPlugin = false
    }

    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("ZeroTickCombinerConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
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