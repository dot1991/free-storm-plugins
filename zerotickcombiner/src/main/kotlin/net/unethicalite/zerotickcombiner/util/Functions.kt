package net.unethicalite.zerotickcombiner.util

import net.runelite.api.Client
import net.storm.api.commons.Time
import net.storm.client.Static
import net.unethicalite.zerotickcombiner.ZeroTickCombiner
import java.util.function.BooleanSupplier
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun ZeroTickCombiner.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun ZeroTickCombiner.sleepUntil(supplier: BooleanSupplier, resetSupplier: BooleanSupplier, pollingRate: Int, timeOut: Int): Boolean {
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