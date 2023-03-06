package net.unethicalite.quickherblore

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.api.events.GameTick
import net.runelite.api.widgets.Widget
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.storm.api.commons.Time
import net.storm.api.entities.NPCs
import net.storm.api.entities.Players
import net.storm.api.entities.TileObjects
import net.storm.api.input.Keyboard
import net.storm.api.items.Bank
import net.storm.api.items.Inventory
import net.storm.api.plugins.LoopedPlugin
import net.storm.api.plugins.Plugins
import net.storm.api.utils.MessageUtils
import net.storm.api.widgets.Dialog
import net.storm.api.widgets.Production
import net.storm.api.widgets.Widgets
import net.storm.client.Static
import net.unethicalite.quickherblore.util.*
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import java.util.function.Predicate
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Quick Herb Cleaner",
    description = "Automatic herb cleaner",
    tags = ["herblore"]
)
class QuickHerblorePlugin : LoopedPlugin() {

    @Inject
    lateinit var config: QuickHerbloreConfig

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
    fun provideConfig(configManager: ConfigManager): QuickHerbloreConfig {
        return configManager.getConfig(QuickHerbloreConfig::class.java)
    }


    override fun startUp() {
        log.info("${this::class.simpleName} started at $startTime")
        chinBreakHandler.registerPlugin(this)
        startPlugin = false
        sleepLength = -1
    }

    override fun shutDown() {
        log.info("${this::class.simpleName} stopped at ${Instant.now()} with runtime $runtime")
        chinBreakHandler.stopPlugin(this)
        chinBreakHandler.unregisterPlugin(this)
    }

    override fun loop(): Int {
        if (!startPlugin || chinBreakHandler.isBreakActive(this) ) return -1


        if (!Bank.isOpen() && Dialog.isOpen())
            Dialog.close()

        with(functions)
        {
            when (getState())
            {
                States.HANDLE_BREAK -> {
                    MessageUtils.addMessage("Attempting to break")
                    chinBreakHandler.startBreak(this@QuickHerblorePlugin)
                }
                States.CLEAN_HERBS -> {
                    if (config.debugToggle())
                        MessageUtils.addMessage("Cleaning state")
                    if (Bank.isOpen())
                    {
                        Bank.close()
                    }

                    if (Inventory.contains(config.cleanHerb().grimyUnnoted))
                    {
                        for (Item in Inventory.getAll { it.id == config.cleanHerb().grimyUnnoted })
                        {
                            Item.interact("Clean")
                            if (!config.fastClean())
                                Time.sleep(75, 100)
                        }
                    }
                    Time.sleepTicksUntil({Dialog.isOpen() || !Inventory.contains(config.cleanHerb().grimyUnnoted)}, 5)
                    return 0
                }
                States.BANKING -> {
                    if (config.debugToggle())
                        MessageUtils.addMessage("Banking state")
                    when (config.methodType()) {
                        MethodType.CLEAN_HERB -> {
                            if (!Inventory.contains(config.cleanHerb().grimyUnnoted)) {
                                if (Bank.isOpen()) {
                                    if (Inventory.contains { it.id != config.cleanHerb().grimyUnnoted }) {
                                        Bank.depositInventory()
                                    }
                                    if (Bank.contains(config.cleanHerb().grimyUnnoted)) {
                                        Bank.withdraw(config.cleanHerb().grimyUnnoted, 28, Bank.WithdrawMode.ITEM)
                                    } else {
                                        stopPlugin("Can't find grimy herb to clean, stopping")
                                    }
                                } else {
                                    var bank: TileObject? = TileObjects.getNearest { it.hasAction("Bank") }
                                    if (bank != null) {
                                        if (!Players.getLocal().isMoving)
                                            bank.interact("Bank")
                                        Time.sleepUntil({Bank.isOpen()}, 5)
                                    } else {
                                        if (config.debugToggle())
                                            stopPlugin("Can't find object with action bank")
                                    }
                                }
                            }
                        }
                    }
                }
                States.IDLE -> {
                    if (config.debugToggle())
                        MessageUtils.addMessage("Idle state")
                }

                else -> {}
            }
        }
        return -1
    }


    private fun stopPlugin() {
        sleepLength = -1
        startPlugin = false
        Plugins.stopPlugin(this)
    }

    private fun stopPlugin(reason: String)
    {
        MessageUtils.addMessage("Stopping for reason: $reason")
        stopPlugin()
    }

    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("QuickHerbloreConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return

        with(functions)
        {
            when (configButtonClicked.key) {
                "startButton" -> {
                    if (!startPlugin) {
                        MessageUtils.addMessage("Successfully authorized")
                        chinBreakHandler.startPlugin(this@QuickHerblorePlugin)
                        startPlugin = true
                    }
                    else
                    {
                        stopPlugin("Stopped plugin")
                    }
                }
            }
        }
    }

}