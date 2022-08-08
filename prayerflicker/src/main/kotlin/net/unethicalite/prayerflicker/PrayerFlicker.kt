package net.unethicalite.prayerflicker

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.coords.WorldPoint
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.api.events.GameStateChanged
import net.runelite.api.events.GameTick
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.runelite.client.util.HotkeyListener
import net.unethicalite.api.entities.Players
import net.unethicalite.api.game.Game
import net.unethicalite.api.movement.pathfinder.Walker
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.api.utils.MessageUtils
import net.unethicalite.api.widgets.Prayers
import net.unethicalite.client.Static
import net.unethicalite.prayerflicker.util.Calculation
import net.unethicalite.prayerflicker.util.Log
import net.unethicalite.prayerflicker.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import java.util.function.Supplier
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Prayer Flicker",
    description = "Automatic prayer flicker",
    tags = ["prayer"]
)
class PrayerFlicker : LoopedPlugin() {

    @Inject
    lateinit var config: PrayerFlickerConfig

    @Inject
    lateinit var calculation: Calculation

    @Inject
    lateinit var chinBreakHandler: ReflectBreakHandler

    var sleepLength: Long = -1

    private var startTime: Instant = Instant.now()

    private val runtime: Duration get() = Duration.between(startTime, Instant.now())

    var startPlugin: Boolean = false

    var shouldhop: Boolean = false

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): PrayerFlickerConfig {
        return configManager.getConfig(PrayerFlickerConfig::class.java)
    }

    override fun startUp() {
        log.info("${this::class.simpleName} started at $startTime")
        chinBreakHandler.registerPlugin(this)
        Static.getKeyManager().registerKeyListener(hotkeyListener)
        reset()
    }

    override fun shutDown() {
        log.info("${this::class.simpleName} stopped at ${Instant.now()} with runtime $runtime")
        chinBreakHandler.unregisterPlugin(this)
        Static.getKeyManager().unregisterKeyListener(hotkeyListener)
        reset()
    }

    override fun loop(): Int {
        return 100
    }

    @Subscribe
    private fun onGameTick(gameTick: GameTick){
        if (!startPlugin || !Game.isLoggedIn()) return

        if (Prayers.isQuickPrayerEnabled())
        {
            Prayers.toggleQuickPrayer(false)
        }
        Prayers.toggleQuickPrayer(true)
    }

    private fun reset() {
        sleepLength = -1
        startPlugin = false
    }

    @Subscribe
    private fun onGameStateChanged(gameStateChanged: GameStateChanged){
        if(gameStateChanged.gameState == GameState.HOPPING){
            shouldhop = false
        }
    }
    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("PrayerFlickerConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
        if (configButtonClicked.key.equals("startHelper", ignoreCase = true)) {
            startPlugin = !startPlugin
            MessageUtils.addMessage("Plugin running: $startPlugin")
            if(startPlugin)
                chinBreakHandler.startPlugin(this)
            else
                chinBreakHandler.stopPlugin(this)
        }
    }

    val hotkeyListener: HotkeyListener = object : HotkeyListener(
        Supplier { config.toggleKeyBind() }) {
        override fun hotkeyPressed() {
            startPlugin = !startPlugin
            if (!startPlugin)
            {
                if (Prayers.isQuickPrayerEnabled())
                {
                    Prayers.toggleQuickPrayer(false)
                }
            }
        }
    }

}