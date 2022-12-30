package net.unethicalite.cannonhelper

import com.google.inject.Provides
import net.storm.api.commons.Time
import net.storm.api.entities.TileObjects
import net.storm.api.items.Inventory
import net.storm.api.movement.Movement
import net.storm.api.plugins.LoopedPlugin
import net.storm.api.utils.MessageUtils
import net.storm.api.widgets.Prayers
import net.runelite.api.*
import net.runelite.api.coords.LocalPoint
import net.runelite.api.coords.WorldPoint
import net.runelite.api.events.ChatMessage
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.unethicalite.cannonhelper.util.Calculation
import net.unethicalite.cannonhelper.util.Functions
import net.unethicalite.cannonhelper.util.Log
import net.unethicalite.cannonhelper.util.ReflectBreakHandler
import org.pf4j.Extension
import java.util.function.BooleanSupplier
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Cannon Helper vKotlin",
    description = "Helpers with cannon stuff",
    tags = ["cannon"]
)
class CannonHelperPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: CannonHelperConfig

    @Inject
    lateinit var client: Client

    @Inject
    lateinit var functions: Functions

    @Inject
    lateinit var calculation: Calculation

    @Inject
    var chinBreakHandler: ReflectBreakHandler? = null

    var sleepLength: Long = -1
    var startPlugin: Boolean = false
    var cannonLocation: WorldPoint? = null
    var safeLocation: WorldPoint? = null
    var randomCount: Int = 0

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): CannonHelperConfig {
        return configManager.getConfig(CannonHelperConfig::class.java)
    }


    override fun startUp() {
        log.info("Starting Cannon helper")
        reset()
    }

    override fun shutDown() {
        log.info("Stopping Cannon helper")
    }

    override fun loop(): Int {
        if(!startPlugin || client.localPlayer == null || client.gameState != GameState.LOGGED_IN) return 1000
        with(functions){
            var cannon: TileObject? = TileObjects.getNearest(cannonLocation, "Dwarf multicannon", "Broken multicannon")
            var player: Player = client.localPlayer

            if(!Inventory.contains(ItemID.CANNONBALL) && !Inventory.contains(ItemID.GRANITE_CANNONBALL)){
                MessageUtils.addMessage("Stopping plugin: Out of balls")
                reset()
                return -1
            }
            if(cannon == null){
                MessageUtils.addMessage("Stopping plugin: Can't find cannon at $cannonLocation")
                reset()
                return -1
            }
            if(cannon.name.contains("Broken")){
                cannon.interact("Repair")
                Time.sleepUntil(BooleanSupplier { cannon.name.contains("Broken") }, 10000)
            } else {
                if(!isFiring()){
                    cannon.interact("Fire")
                    Time.sleepUntil(BooleanSupplier { isRunning }, 5000)
                }else{
                    if(getBalls() <= randomCount){
                        cannon.interact("Fire")
                        if(Time.sleepUntil({ getBalls() >= 25 }, 3000)){
                            randomCount = calculation.getRandomIntBetweenRange(config.minBalls(), config.maxBalls())
                        }
                    }
                }
            }
            if(safeLocation != null){
                if(!player.localLocation.equals(safeLocation) && isFiring() && !player.isMoving)
                    Movement.walkTo(safeLocation)
            }

            if(config.restorePrayer() && Prayers.getPoints() <= 20 && Inventory.contains(*config.prayerType().ids)){
                Inventory.getFirst(*config.prayerType().ids).interact("Drink")
            }
            return 600
        }
    }

    private fun reset() {
        sleepLength = -1
        randomCount = 0
        cannonLocation = null
        safeLocation = null
        startPlugin = false
    }

    @Subscribe
    private fun onChatMessage(chatMessage: ChatMessage){
        var message: String = chatMessage.message
        if(message.isEmpty() || chatMessage.type != ChatMessageType.GAMEMESSAGE) return
        if(message.contains("That isn't your cannon!", false)){
            MessageUtils.addMessage("Plugin interacted with wrong cannon, stopping.")
            reset()
        }
    }
    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("CannonHelperConfig", ignoreCase = true) || client.gameState != GameState.LOGGED_IN || client.localPlayer == null) return
        if (configButtonClicked.key.equals("getTile", ignoreCase = true)) {
            cannonLocation = TileObjects.getNearest("Dwarf multicannon", "Broken multicannon").worldLocation
            MessageUtils.addMessage("Cannon location set to nearest cannon!")
            MessageUtils.addMessage("World Location: $cannonLocation")
        }

        if (configButtonClicked.key.equals("setSafeTile", ignoreCase = true)) {
            safeLocation = client.localPlayer.worldLocation
            MessageUtils.addMessage("Safespot location set!")
            MessageUtils.addMessage("World Location: $safeLocation")
        }

        if (configButtonClicked.key.equals("startHelper", ignoreCase = true)) {
            if (cannonLocation == null) {
                MessageUtils.addMessage("Set cannon tile location before starting please!")
                return
            }
            if (config.minBalls() > config.maxBalls()) {
                MessageUtils.addMessage("Why is the minimum balls greater than the maximum balls? spastic")
                return
            }
            if (!startPlugin) {
                MessageUtils.addMessage("Cannon Helper Started")
                startPlugin = true
                randomCount = calculation.getRandomIntBetweenRange(config.minBalls(), config.maxBalls())
            } else {
                MessageUtils.addMessage("Cannon Helper stopped")
                startPlugin = false
            }
        }
    }


}