package net.unethicalite.thief.util

import net.runelite.api.Client
import net.runelite.api.ItemID
import net.runelite.api.Skill
import net.runelite.api.TileObject
import net.unethicalite.api.entities.Players
import net.unethicalite.api.entities.TileObjects
import net.unethicalite.api.game.Game
import net.unethicalite.api.items.Bank
import net.unethicalite.api.items.Equipment
import net.unethicalite.api.items.Inventory
import net.unethicalite.client.Static
import net.unethicalite.thief.States
import net.unethicalite.thief.Hunter
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun Hunter.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun Hunter.getState(): States {
        if (!Game.isLoggedIn()) return States.UNKNOWN

        if (chinBreakHandler.shouldBreak(this))
            return States.HANDLE_BREAK
        if (Inventory.contains { it.id == 10012 })
            return States.CATCH
        return States.RELEASE
    }

}