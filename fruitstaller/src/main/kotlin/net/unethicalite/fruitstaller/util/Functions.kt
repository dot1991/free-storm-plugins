package net.unethicalite.fruitstaller.util

import net.runelite.api.Client
import net.unethicalite.api.game.Game
import net.unethicalite.api.items.Inventory
import net.unethicalite.fruitstaller.FruitStallerPlugin
import net.unethicalite.fruitstaller.States
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun FruitStallerPlugin.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun FruitStallerPlugin.getState(): States {
        if (!Game.isLoggedIn()) return States.UNKNOWN
        if (chinBreakHandler.shouldBreak(this))
            return States.HANDLE_BREAK
        if(Inventory.isFull())
            return States.DROP
        return States.STEAL
    }

}