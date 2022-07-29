package net.unethicalite.woodcutter

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.events.ChatMessage
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
import net.unethicalite.api.widgets.Production
import net.unethicalite.client.Static
import net.unethicalite.woodcutter.util.Calculation
import net.unethicalite.woodcutter.util.Functions
import net.unethicalite.woodcutter.util.Log
import net.unethicalite.woodcutter.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "WoodCutter",
    description = "Automatic Woodcutter",
    tags = ["woodcutting"]
)
class WoodCutterPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: WoodCutterConfig

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
    fun provideConfig(configManager: ConfigManager): WoodCutterConfig {
        return configManager.getConfig(WoodCutterConfig::class.java)
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
            if (config.debugger()) MessageUtils.addMessage("Current state: ${getState()}")
            var local: Player = Players.getLocal()

            when(getState()){
                States.HANDLE_BREAK -> {
                    MessageUtils.addMessage("Attempting to break")
                    chinBreakHandler.startBreak(this@WoodCutterPlugin)
                }
                States.CUT_TREE -> {
                    val tree: TileObject? = TileObjects.getNearest { it.name.equals(config.treeType().tree) && it.distanceTo(Players.getLocal()) < 10 }
                    tree?.let {
                        it.interact("Chop down")
                        Time.sleepUntil({Players.getLocal().isAnimating}, 2000)
                    }
                }
                States.DROP_INVENTORY -> {
                    for(Item in Inventory.getAll { it.id == ItemID.WILLOW_LOGS }){
                        Item.interact("Drop")
                        Time.sleep(calculation.getRandomIntBetweenRange(50,100).toLong())
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
        if (!configButtonClicked.group.equals("WoodCutterConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
        if (configButtonClicked.key.equals("startHelper", ignoreCase = true)) {
            startPlugin = !startPlugin
            MessageUtils.addMessage("Plugin running: $startPlugin")
            if(startPlugin) chinBreakHandler.startPlugin(this) else chinBreakHandler.stopPlugin(this)
        }
    }

}