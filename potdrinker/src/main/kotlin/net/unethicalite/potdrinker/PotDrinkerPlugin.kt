package net.unethicalite.potdrinker

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.storm.api.commons.Time
import net.storm.api.entities.NPCs
import net.storm.api.entities.Players
import net.storm.api.game.Skills
import net.storm.api.items.Bank
import net.storm.api.items.Inventory
import net.storm.api.plugins.LoopedPlugin
import net.storm.api.utils.MessageUtils
import net.storm.api.widgets.Dialog
import net.storm.api.widgets.Production
import net.storm.client.Static
import net.unethicalite.potdrinker.util.*
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "NMZ Pot drinker",
    description = "Automatic pot drinker",
    tags = ["pot"]
)
class PotDrinkerPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: PotDrinkerConfig

    @Inject
    lateinit var functions: Functions

    @Inject
    lateinit var calculation: Calculation

    @Inject
    lateinit var client: Client

    @Inject
    lateinit var chinBreakHandler: ReflectBreakHandler

    var sleepLength: Long = -1

    private var startTime: Instant = Instant.now()

    private val runtime: Duration get() = Duration.between(startTime, Instant.now())

    var startPlugin: Boolean = false

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): PotDrinkerConfig {
        return configManager.getConfig(PotDrinkerConfig::class.java)
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
        if (chinBreakHandler.isBreakActive(this)) return 100

        if (client.getVarbitValue(3955) == 0)
        {
            var item: Item? = Inventory.getFirst { it.name.contains("Overload") }
            if (item != null)
            {
                item.interact("Drink")
                return -2
            }
        }
        return -1
    }

    private fun reset() {
        sleepLength = -1
        startPlugin = false
    }

    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("GemCutterConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
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