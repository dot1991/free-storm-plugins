package net.unethicalite.miner

import com.google.inject.Provides
import net.runelite.api.GameState
import net.runelite.api.TileObject
import net.runelite.api.coords.WorldPoint
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.items.Inventory
import net.unethicalite.api.items.Items
import net.unethicalite.api.magic.Magic
import net.unethicalite.api.magic.Spell
import net.unethicalite.api.magic.SpellBook
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.api.utils.MessageUtils
import net.unethicalite.api.widgets.Widgets
import net.unethicalite.client.Static
import net.unethicalite.miner.util.Calculation
import net.unethicalite.miner.util.Functions
import net.unethicalite.miner.util.Log
import net.unethicalite.miner.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Miner",
    description = "Automatic Miner",
    tags = ["mining"]
)
class MinerPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: MinerConfig

    @Inject
    lateinit var functions: Functions

    @Inject
    lateinit var calculation: Calculation

    @Inject
    lateinit var chinBreakHandler: ReflectBreakHandler

    var sleepLength: Long = -1
    var startLocation: WorldPoint? = null

    private var startTime: Instant = Instant.now()

    private val runtime: Duration get() = Duration.between(startTime, Instant.now())

    var startPlugin: Boolean = false

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): MinerConfig {
        return configManager.getConfig(MinerConfig::class.java)
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
            when(getState()){
                States.HANDLE_BREAK -> {
                    MessageUtils.addMessage("Attempting to break")
                    chinBreakHandler.startBreak(this@MinerPlugin)
                }
                States.MINE_ROCK -> {
                    val tree: TileObject? = TileObjects.getNearest { config.rockType().rockId.contains(it.id) && it.distanceTo(startLocation) < config.radius() }
                    tree?.let {
                        it.interact("Mine")
                        Time.sleepUntil({Players.getLocal().isAnimating}, 1350)
                    }
                }
                States.DROP_INVENTORY -> {
                    for(Item in Inventory.getAll { it.id == config.rockType().item  || "Uncut" in it.name }){
                        Item.interact("Drop")
                        Time.sleep(calculation.getRandomIntBetweenRange(25,40).toLong())
                    }
                }
            }
            return sleepDelay().toInt()
        }
    }

    private fun reset() {
        sleepLength = -1
        startPlugin = false
        startLocation = null
    }

    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("MinerConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
        if (configButtonClicked.key.equals("startHelper", ignoreCase = true)) {
            startPlugin = !startPlugin
            startLocation = Players.getLocal().worldLocation
            MessageUtils.addMessage("Plugin running: $startPlugin")
            if(startPlugin)
                chinBreakHandler.startPlugin(this)
            else
                chinBreakHandler.stopPlugin(this)
        }
    }

}