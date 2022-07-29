package net.unethicalite.woodcutter.util

import net.runelite.api.Client
import net.runelite.api.TileObject
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.items.Inventory
import net.unethicalite.client.Static
import net.unethicalite.woodcutter.WoodCutterPlugin
import net.unethicalite.woodcutter.States
import java.util.function.BooleanSupplier
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun WoodCutterPlugin.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun WoodCutterPlugin.getState(): States {
        if(chinBreakHandler.shouldBreak(this)){
            return States.HANDLE_BREAK
        }
        if(Inventory.isFull()){
            return States.DROP_INVENTORY
        }
        if(!Players.getLocal().isAnimating){
            val tree: TileObject? = TileObjects.getNearest { it.name.equals(config.treeType().tree) && it.distanceTo(Players.getLocal()) < 10 }
                ?: return States.UNKNOWN
            return States.CUT_TREE
        }
        return States.UNKNOWN
    }

    fun WoodCutterPlugin.sleepUntil(supplier: BooleanSupplier, resetSupplier: BooleanSupplier, pollingRate: Int, timeOut: Int): Boolean {
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