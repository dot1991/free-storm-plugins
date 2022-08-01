package net.unethicalite.serumnaughtyedition.util

import net.runelite.api.Client
import net.runelite.api.TileObject
import net.unethicalite.api.commons.Time
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.items.Inventory
import net.unethicalite.client.Static
import net.unethicalite.serumnaughtyedition.SerumNaughtyEdition
import net.unethicalite.serumnaughtyedition.States
import java.util.function.BooleanSupplier
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun SerumNaughtyEdition.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun SerumNaughtyEdition.sleepUntil(supplier: BooleanSupplier, resetSupplier: BooleanSupplier, pollingRate: Int, timeOut: Int): Boolean {
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