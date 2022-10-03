package net.unethicalite.thief

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
import net.unethicalite.api.movement.Movement
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
    name = "Hunter",
    description = "Automatic Hunter",
    tags = ["Hunter"]
)
class Hunter : LoopedPlugin() {

    @Inject
    lateinit var config: HunterConfig

    @Inject
    lateinit var functions: Functions

    @Inject
    lateinit var calculation: Calculation

    @Inject
    lateinit var chinBreakHandler: ReflectBreakHandler

    var sleepLength: Long = -1

    var startPoint: WorldPoint? = null

    private var startTime: Instant = Instant.now()

    private val runtime: Duration get() = Duration.between(startTime, Instant.now())

    var startPlugin: Boolean = false

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): HunterConfig {
        return configManager.getConfig(HunterConfig::class.java)
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
            MessageUtils.addMessage("State: " + getState().name)

            when(getState()){
                States.HANDLE_BREAK -> {
                    MessageUtils.addMessage("Attempting to break")
                    chinBreakHandler.startBreak(this@Hunter)
                }
                States.CATCH -> {
                    var butter: NPC? = NPCs.getNearest { it.id == 5556 }
                    if (butter != null && !Players.getLocal().isMoving && !Players.getLocal().isAnimating)
                    {
                        butter.interact("Catch")
                    }
                    return -1
                }
                States.RELEASE -> {
                    for (item in Inventory.getAll { it.id == 10020 }){
                        item?.interact("Release")
                        Time.sleep(25)
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
        if (!configButtonClicked.group.equals("HunterConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
        if (configButtonClicked.key.equals("startHelper", ignoreCase = true)) {
            startPlugin = !startPlugin
            MessageUtils.addMessage("Plugin running: $startPlugin")
            if(startPlugin)
            {
                startPoint = Players.getLocal().worldLocation
                chinBreakHandler.startPlugin(this)
            }
            else
            {
                startPoint = null
                chinBreakHandler.stopPlugin(this)
            }
        }
    }

}