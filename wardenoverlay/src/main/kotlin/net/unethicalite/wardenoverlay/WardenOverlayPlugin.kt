package net.unethicalite.wardenoverlay

import WardenOverlayOverlay
import com.google.inject.Provides
import lombok.extern.slf4j.Slf4j
import net.runelite.api.Client
import net.runelite.api.GameObject
import net.runelite.api.events.GameTick
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.runelite.client.ui.overlay.OverlayManager
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.wardenoverlay.util.Calculation
import net.unethicalite.wardenoverlay.util.Log
import net.unethicalite.wardenoverlay.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Warden Overlay",
    description = "Warden Overlay",
    tags = ["warden"]
)
@Slf4j
class WardenOverlayPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: WardenOverlayConfig

    @Inject
    lateinit var calculation: Calculation

    @Inject
    lateinit var client: Client

    @Inject
    lateinit var chinBreakHandler: ReflectBreakHandler

    @Inject
    lateinit var overlayManager: OverlayManager

    @Inject
    lateinit var wardenOverlayOverlay: WardenOverlayOverlay

    var sleepLength: Long = -1

    private var startTime: Instant = Instant.now()

    private val runtime: Duration get() = Duration.between(startTime, Instant.now())

    var startPlugin: Boolean = false

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): WardenOverlayConfig {
        return configManager.getConfig(WardenOverlayConfig::class.java)
    }

    override fun startUp() {
        log.info("${this::class.simpleName} started at $startTime")
        chinBreakHandler.registerPlugin(this)
        overlayManager.add(wardenOverlayOverlay)
        reset()
    }

    override fun shutDown() {
        log.info("${this::class.simpleName} stopped at ${Instant.now()} with runtime $runtime")
        chinBreakHandler.unregisterPlugin(this)
        overlayManager.remove(wardenOverlayOverlay)
        reset()
    }

    override fun loop(): Int {
        return 100
    }

    @Subscribe
    private fun onGameTick(gameTick: GameTick){

    }

    private fun reset() {
        sleepLength = -1
    }

}