package net.unethicalite.museumcleaner

import com.google.inject.Provides
import net.runelite.api.*
import net.runelite.api.coords.WorldArea
import net.runelite.api.coords.WorldPoint
import net.runelite.api.events.ChatMessage
import net.runelite.api.events.ConfigButtonClicked
import net.runelite.api.widgets.Widget
import net.runelite.client.config.ConfigManager
import net.runelite.client.eventbus.Subscribe
import net.runelite.client.plugins.PluginDescriptor
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.NPCs
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.items.Inventory
import net.unethicalite.api.plugins.LoopedPlugin
import net.unethicalite.api.utils.MessageUtils
import net.unethicalite.api.widgets.Dialog
import net.unethicalite.api.widgets.Widgets
import net.unethicalite.client.Static
import net.unethicalite.museumcleaner.util.Calculation
import net.unethicalite.museumcleaner.util.Functions
import net.unethicalite.museumcleaner.util.Log
import net.unethicalite.museumcleaner.util.ReflectBreakHandler
import org.pf4j.Extension
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@Extension
@PluginDescriptor(
    name = "Museum Cleaner",
    description = "Automatic Museum Cleaner",
    tags = ["museum"]
)
class MuseumCleanerPlugin : LoopedPlugin() {

    @Inject
    lateinit var config: MuseumCleanerConfig

    @Inject
    lateinit var functions: Functions

    @Inject
    lateinit var calculation: Calculation

    @Inject
    lateinit var chinBreakHandler: ReflectBreakHandler

    var sleepLength: Long = -1

    private var startTime: Instant = Instant.now()

    private val runtime: Duration get() = Duration.between(startTime, Instant.now())


    var cleaned: IntArray = intArrayOf(11181, 11178, 11177, 11183, 11195, 11179, 11180, 11175, ItemID.ARROWHEADS)
    var keep: IntArray = intArrayOf(670, 676, 675, 995, 1059, 1061, ItemID.ANTIQUE_LAMP_11189)
    var startPlugin: Boolean = false
    var cleaningArea: WorldArea = WorldArea(WorldPoint(3253, 3442,0), WorldPoint( 3267, 3447,0));

    companion object : Log()

    @Provides
    fun provideConfig(configManager: ConfigManager): MuseumCleanerConfig {
        return configManager.getConfig(MuseumCleanerConfig::class.java)
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
        if(!startPlugin || chinBreakHandler.isBreakActive(this)) return 600
        with(functions) {
            if(config.debugger()) MessageUtils.addMessage("Current state: ${getState()}")
            when(getState()){
                States.HANDLE_BREAK -> {
                    MessageUtils.addMessage("Attempting to break")
                    chinBreakHandler.startBreak(this@MuseumCleanerPlugin)
                }
                States.USE_LAMP -> {
                    val lampInterface: Widget? = Widgets.get(240, 0)
                    val skillName: String = config.xpSkill().getName()
                    val confirmInterface: Widget? = Widgets.get(240, 26)
                    if(lampInterface != null && lampInterface.isVisible){
                        if(confirmInterface?.getChild(0) != null && confirmInterface.getChild(0).text.contains(skillName)){
                            confirmInterface.interact("Confirm")
                            return sleepDelay().toInt()
                        }else{
                            Widgets.get(240) { it.hasAction(skillName) }?.interact(skillName)
                            return sleepDelay().toInt()
                        }
                    }else{
                        Inventory.getFirst { it.id == ItemID.ANTIQUE_LAMP_11189 }.interact("Rub")
                        Time.sleepUntil({ lampInterface != null } , 2000)
                    }
                }
                States.WEAR_BOOTS -> {
                    Inventory.getFirst { it.name.contains("Leather boots") }.interact("Wear")
                }
                States.WEAR_GLOVES -> {
                    Inventory.getFirst { it.name.contains("Leather gloves") }.interact("Wear")
                }
                States.GET_TOOLS -> {
                    val wall: TileObject? = TileObjects.getNearest("Tools")
                    if(!Dialog.isOpen()){
                        wall?.interact("Take")
                    }else{
                        Dialog.chooseOption("Yes")
                    }
                }
                States.DROP_TRASH -> {
                    for (Item in Inventory.getAll { !cleaned.contains(it.id) && !keep.contains(it.id)}){
                        Item.interact("Drop")
                        Time.sleep(150)
                    }
                }
                States.GET_FINDS -> {
                    if(Players.getLocal().animation != 827 && !Inventory.isFull()) {
                        TileObjects.getFirstAt(3263, 3446, 0, "Dig Site specimen rocks")?.interact("Take")
                    }
                }
                States.CLEAN_FIND -> {
                    val table: TileObject? = TileObjects.getNearest(24556)
                    if(Players.getLocal().animation == -1 && table != null){
                        table.interact("Clean")
                    }
                }
                States.TURN_IN -> {
                    if(!Dialog.isOpen() && !Players.getLocal().isMoving) {
                        TileObjects.getNearest(24534)?.interact("Add finds")
                    }
                    if(Dialog.isOpen() && Dialog.chooseOption(1)){
                        sleepUntil({ !Inventory.contains(*cleaned) }, { Players.getLocal().animation != -1  }, 100, 2000)
                    }
                }
                else -> {
                    log.info("Bad state reached")
                    return 600
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
    private fun onChatMessage(chatMessage: ChatMessage){
        val message: String = chatMessage.message
        if(message.isEmpty() || chatMessage.type != ChatMessageType.GAMEMESSAGE) return

    }
    @Subscribe
    private fun onConfigButtonPressed(configButtonClicked: ConfigButtonClicked) {
        if (!configButtonClicked.group.equals("MuseumCleanerConfig", ignoreCase = true) || Static.getClient().gameState != GameState.LOGGED_IN || Players.getLocal() == null) return
        if (configButtonClicked.key.equals("startHelper", ignoreCase = true)) {
            startPlugin = !startPlugin
            MessageUtils.addMessage("Plugin running: $startPlugin")
            if(startPlugin) chinBreakHandler.startPlugin(this) else chinBreakHandler.stopPlugin(this)
        }
    }

}