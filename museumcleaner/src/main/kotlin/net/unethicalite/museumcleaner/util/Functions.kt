package net.unethicalite.museumcleaner.util

import net.runelite.api.Client
import net.runelite.api.ItemID
import net.runelite.api.Player
import net.runelite.api.TileObject
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.items.Equipment
import net.unethicalite.api.items.Inventory
import net.unethicalite.client.Static
import net.unethicalite.museumcleaner.MuseumCleanerPlugin
import net.unethicalite.museumcleaner.States
import java.util.function.BooleanSupplier
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun MuseumCleanerPlugin.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun MuseumCleanerPlugin.getState(): States {

        if(chinBreakHandler.shouldBreak(this)) return States.HANDLE_BREAK

        val local: Player = Players.getLocal()

        if(!cleaningArea.contains(Players.getLocal().worldLocation)) return States.UNKNOWN
        if(Inventory.contains { it.name.contains("Clean necklace") }) return States.UNKNOWN

        if(Inventory.contains { it.id == ItemID.ANTIQUE_LAMP_11189 }) return States.USE_LAMP
        if(!Inventory.contains("Trowel") || !Inventory.contains("Rock pick") || !Inventory.contains("Specimen brush") || (!Inventory.contains("Leather boots") && !Equipment.contains("Leather boots")) || (!Inventory.
                    contains("Leather gloves") && !Equipment.contains("Leather gloves")))
            return States.GET_TOOLS
        if(Inventory.contains("Leather boots") && !Equipment.contains("Leather boots")) return States.WEAR_BOOTS
        if(Inventory.contains("Leather gloves") && !Equipment.contains("Leather gloves")) return States.WEAR_GLOVES

        if(Inventory.contains{ !keep.contains(it.id) && !cleaned.contains(it.id) }) return States.DROP_TRASH

        val rock: TileObject? = TileObjects.getFirstAt(3263, 3446, 0, "Dig Site specimen rocks")
        if(Inventory.contains { cleaned.contains(it.id) } && !Inventory.contains { it.name.contains("Uncleaned find") }) return States.TURN_IN
        if(!Inventory.contains("Uncleaned find") || (rock != null && !Inventory.isFull() && rock.distanceTo(local) == 1)) return States.GET_FINDS //a = 827

        if(Inventory.contains("Uncleaned find")) {        // 6217 6459
            if(local.animation == 6217 || local.animation == 6459) return States.UNKNOWN
            return States.CLEAN_FIND
        }

        return States.UNKNOWN
    }

    fun sleepUntil(supplier: BooleanSupplier, resetSupplier: BooleanSupplier, pollingRate: Int, timeOut: Int): Boolean {
        if (Static.getClient().isClientThread) {
            return false
        }
        var start = System.currentTimeMillis()
        while (!supplier.asBoolean) {
            if (System.currentTimeMillis() > start + timeOut) {
                return false
            }
            if (resetSupplier.asBoolean) {
                start = System.currentTimeMillis()
            }
            if (!Time.sleep(pollingRate.toLong())) {
                return false
            }
        }
        return true
    }
}