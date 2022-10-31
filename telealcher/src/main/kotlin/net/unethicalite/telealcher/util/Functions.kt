package net.unethicalite.telealcher.util

import net.runelite.api.Client
import net.unethicalite.api.entities.Players
import net.unethicalite.api.game.Game
import net.unethicalite.api.items.Inventory
import net.unethicalite.telealcher.TeleAlcherPlugin
import net.unethicalite.telealcher.States
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun TeleAlcherPlugin.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun TeleAlcherPlugin.getState(): States {
        if (!Game.isLoggedIn()) return States.UNKNOWN
        if (chinBreakHandler.shouldBreak(this))
            return States.HANDLE_BREAK
        if (Players.getLocal().graphic == 113 || Players.getLocal().graphic == 112 || (!config.toggleAlch() && Players.getLocal().graphic != 111))
            return States.TELE
        if (config.toggleAlch() && !Players.getLocal().isAnimating)
            return States.ALCH
        return States.UNKNOWN
    }

}