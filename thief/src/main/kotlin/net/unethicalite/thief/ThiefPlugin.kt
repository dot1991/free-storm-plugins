package net.unethicalite.thief

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.coords.WorldPoint
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.storm.api.commons.Time
import net.storm.api.entities.NPCs
import net.storm.api.entities.Players
import net.storm.api.entities.TileObjects
import net.storm.api.game.Game
import net.storm.api.items.Bank
import net.storm.api.items.Equipment
import net.storm.api.items.Inventory
import net.storm.api.movement.Movement
import net.storm.api.movement.Reachable
import net.storm.api.movement.pathfinder.model.BankLocation
import net.storm.api.plugins.LoopedPlugin
import net.storm.api.plugins.Plugins
import net.storm.api.utils.MessageUtils
import net.storm.api.widgets.Dialog
import net.storm.api.widgets.Prayers
import net.storm.client.Static
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

    @Inject
    lateinit var configManager: ConfigManager

    var sleepLength: Long = -1
    var returnCoord: WorldPoint? = null
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
                States.BANK -> {
                    if (Bank.isOpen())
                    {
                        if (Inventory.isFull())
                        {
                            Bank.depositInventory()
                        }
                    }
                    else
                    {
                        var pos = BankLocation.getNearest()
                        if (isNearBank())
                        {
                            openBank()
                        }
                        else
                        {
                            Movement.walkTo(pos)
                        }
                    }
                    return -1
                }
                States.RETURN -> {
                    if (returnCoord != null)
                    {
                        Movement.walkTo(returnCoord)
                    }
                    return -2
                }
                States.DROP -> {
                    if (config.stall().item.any { it == -1 })
                    {
                        for (item in Inventory.getAll()){
                            item.interact("Drop")
                            Time.sleep(75)
                        }
                    }
                    else{
                        for (item in Inventory.getAll { it.id in config.stall().item })
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
        returnCoord = null
    }

    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("ThiefConfig", ignoreCase = true) || !Game.isLoggedIn() || Players.getLocal() == null) return
        when (configButtonClicked.key)
        {
            "startButton" -> {
                if (!startPlugin)
                {
                    if (config.returnCoord().isNotEmpty() && config.shouldBank())
                    {
                        var args = config.returnCoord().split(",")
                        if (args.size == 3)
                        {
                            returnCoord = WorldPoint(args[0].toInt(), args[1].toInt(), args[2].toInt())
                        }
                        else
                        {
                            MessageUtils.addMessage("Incorrect coordinate format, please empty the box and refetch the return coordinate.")
                            return
                        }
                    }
                    startPlugin = true
                    chinBreakHandler.startPlugin(this)
                }
                else
                {
                    stopPlugin("Stopping plugin")
                    chinBreakHandler.stopPlugin(this)
                }
            }
            "fetchCoord" -> {
                configManager.setConfiguration("ThiefConfig", "returnCoord", "${Players.getLocal().worldLocation.x},${Players.getLocal().worldLocation.y},${Players.getLocal().worldLocation.plane}")
            }
        }
    }

    private fun stopPlugin() {
        reset()
        Plugins.stopPlugin(this)
        stop()
    }

    private fun stopPlugin(reason: String) {
        MessageUtils.addMessage("Stopping for reason: $reason")
        log.info("Stopping for reason: $reason")
        stopPlugin()
    }

    fun openBank() {
        var bank: TileObject? = TileObjects.getSurrounding(Players.getLocal().worldLocation, 15) { (it.hasAction("Bank") || (it.name.lowercase().contains("bank") && it.hasAction("Use"))) }.minByOrNull { it.worldLocation.distanceToHypotenuse(Players.getLocal().worldLocation) }

        if (!Players.getLocal().isMoving) {
            if (bank != null && Reachable.isInteractable(bank)) {
                bank.interact(if (bank.hasAction("Use")) "Use" else "Bank")
            } else {
                Movement.walkTo(BankLocation.getNearest())
            }
        }
    }

    fun isNearBank(): Boolean {
        return TileObjects.getFirstSurrounding(BankLocation.getNearest().area.toWorldPoint(), 15) {it.hasAction("Bank") || (it.name.lowercase().contains("bank") && it.hasAction("Collect"))} != null
    }
}