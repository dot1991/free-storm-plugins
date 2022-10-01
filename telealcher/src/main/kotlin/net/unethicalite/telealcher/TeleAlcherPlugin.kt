package net.unethicalite.telealcher

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.game.Skills
import net.unethicalite.api.items.Inventory
import net.unethicalite.api.magic.Magic
import net.unethicalite.api.magic.SpellBook
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.api.utils.MessageUtils
import net.unethicalite.client.Static
import net.unethicalite.telealcher.util.Calculation
import net.unethicalite.telealcher.util.Functions
import net.unethicalite.telealcher.util.Log
import net.unethicalite.telealcher.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Tele Alcher",
    description = "Automatic tele alcher",
    tags = ["tele", "alch"]
)
class TeleAlcherPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: TeleAlcherConfig

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
    fun provideConfig(configManager: ConfigManager): TeleAlcherConfig {
        return configManager.getConfig(TeleAlcherConfig::class.java)
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
                    chinBreakHandler.startBreak(this@TeleAlcherPlugin)
                }
                States.TELE -> {
                    if (config.teleport() != null && Skills.getLevel(Skill.MAGIC) >= config.teleport()!!.requiredLevel){
                        Magic.cast(config.teleport()?.teleport)
                    }
                    return -1
                }
                States.ALCH -> {
                    val alchItem: Item? = Inventory.getFirst { it.id == config.alchId() }
                    if (Skills.getLevel(Skill.MAGIC) < 55)
                    {
                        Magic.cast(SpellBook.Standard.LOW_LEVEL_ALCHEMY, alchItem)

                    }
                    else
                    {
                        Magic.cast(SpellBook.Standard.HIGH_LEVEL_ALCHEMY, alchItem)
                    }
                    return 300
                }
                States.UNKNOWN -> {
                    MessageUtils.addMessage("Reached unknown")
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
        if (!configButtonClicked.group.equals("TeleAlcherConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
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