package net.unethicalite.glassblower.util

import net.runelite.api.Client
import net.runelite.api.ItemID
import net.runelite.api.TileObject
import net.storm.api.entities.Players
import net.storm.api.entities.TileObjects
import net.storm.api.game.Game
import net.storm.api.items.Inventory
import net.unethicalite.glassblower.GlassBlowerPlugin
import net.unethicalite.glassblower.States
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun GlassBlowerPlugin.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun GlassBlowerPlugin.getState(): States {
        if (!Game.isLoggedIn()) return States.UNKNOWN
        if (chinBreakHandler.shouldBreak(this))
            return States.HANDLE_BREAK
        if (!Inventory.contains(ItemID.MOLTEN_GLASS) || !Inventory.contains(ItemID.GLASSBLOWING_PIPE))
            return States.HANDLE_BANK
        if (!Players.getLocal().isAnimating && Inventory.contains(ItemID.MOLTEN_GLASS))
            return States.BLOW_GLASS
        return States.UNKNOWN
    }

}