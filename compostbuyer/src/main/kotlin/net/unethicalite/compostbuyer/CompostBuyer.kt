package net.unethicalite.compostbuyer

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.api.events.GameStateChanged
import net.runelite.api.widgets.Widget
import net.runelite.api.widgets.WidgetInfo
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.NPCs
import net.unethicalite.api.entities.Players
import net.unethicalite.api.game.Game
import net.unethicalite.api.game.Worlds
import net.unethicalite.api.items.Bank
import net.unethicalite.api.items.Inventory
import net.unethicalite.api.items.Shop
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.api.utils.MessageUtils
import net.unethicalite.api.widgets.Widgets
import net.unethicalite.client.Static
import net.unethicalite.compostbuyer.util.Calculation
import net.unethicalite.compostbuyer.util.Functions
import net.unethicalite.compostbuyer.util.Log
import net.unethicalite.compostbuyer.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Compost Buyer",
    description = "Automatic Compost Buyer",
    tags = ["compost"]
)
class CompostBuyer : LoopedPlugin() {

    @Inject
    lateinit var config: CompostBuyerConfig

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

    var shouldhop: Boolean = false

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): CompostBuyerConfig {
        return configManager.getConfig(CompostBuyerConfig::class.java)
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
        if (!Game.isLoggedIn() || !startPlugin || chinBreakHandler.isBreakActive(this)) return 100

        with(functions) {

            if(chinBreakHandler.shouldBreak(this@CompostBuyer)){
                MessageUtils.addMessage("Attempting to break")
                chinBreakHandler.startBreak(this@CompostBuyer)
            }

            if(!shouldhop){
                if (Inventory.contains(19704))
                {
                    for (Item in Inventory.getAll { it.id == 19704 })
                    {
                        Item.interact("Open")
                        Time.sleep(75)
                    }
                    return 1
                }
                if (!Shop.isOpen())
                {
                    var shop: NPC? = NPCs.getNearest("Vanessa");

                    if (!Players.getLocal().isMoving)
                    {
                        shop?.let {
                            it.interact("Trade")
                        }
                    }
                    Time.sleepUntil({Shop.isOpen()}, 2500)
                }
                else {
                    if (Shop.getStock(19704) == 0) {
                        shouldhop = true
                    } else {
                        Shop.buyTen(19704)
                        shouldhop = true
                        return -1
                    }
                }
            }else{
                if (Shop.isOpen())
                {
                    close()
                }
                else
                {
                    Worlds.hopTo(Worlds.getRandom { it.isMembers && it.isNormal })
                }
                return 100
            }

            return sleepDelay().toInt()
        }
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
        if (!configButtonClicked.group.equals("CompostBuyerConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
        if (configButtonClicked.key.equals("startHelper", ignoreCase = true)) {
            startPlugin = !startPlugin
            MessageUtils.addMessage("Plugin running: $startPlugin")
            if(startPlugin)
                chinBreakHandler.startPlugin(this)
            else
                chinBreakHandler.stopPlugin(this)
        }
    }

    private fun close(){
        val close: Widget? = Widgets.get(WidgetInfo.SHOP_ITEMS_CONTAINER.groupId).first { it.hasAction("Close") }
        close?.let {
            it.interact("Close")
        }
    }
}