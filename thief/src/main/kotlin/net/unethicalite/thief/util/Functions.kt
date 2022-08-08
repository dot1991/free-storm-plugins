package net.unethicalite.thief.util

import net.runelite.api.Client
import net.runelite.api.ItemID
import net.runelite.api.Skill
import net.unethicalite.api.entities.Players
import net.unethicalite.api.game.Game
import net.unethicalite.api.items.Bank
import net.unethicalite.api.items.Equipment
import net.unethicalite.api.items.Inventory
import net.unethicalite.client.Static
import net.unethicalite.thief.States
import net.unethicalite.thief.ThiefPlugin
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun ThiefPlugin.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun ThiefPlugin.getState(): States {
        if (!Game.isLoggedIn()) return States.UNKNOWN

        if (chinBreakHandler.shouldBreak(this))
            return States.HANDLE_BREAK

        val curr: Int = Static.getClient().getBoostedSkillLevel(Skill.HITPOINTS)
        val max: Int = Static.getClient().getRealSkillLevel(Skill.HITPOINTS)

        if (Players.getLocal().graphic == 245)
        {
            if (Inventory.contains { it.id == ItemID.JUG })
                return States.DROP_JUG
            if(Inventory.contains {"Coin pouch" in it.name} && Inventory.getCount(true, "Coin pouch") >= 10)
                return States.OPEN_POUCH
            if (curr < (max / 2))
                return States.EAT_FOOD
            return States.UNKNOWN
        }

        if (curr < (max / 2))
            return States.EAT_FOOD
        if(!Equipment.contains { it.id == ItemID.DODGY_NECKLACE } && Inventory.contains { it.id == ItemID.DODGY_NECKLACE })
            return States.EQUIP_NECKLACE
        if (!Inventory.contains { it.id == ItemID.JUG_OF_WINE })
            return States.HANDLE_BANK
        if (Bank.isOpen() && Equipment.contains { it.id == ItemID.DODGY_NECKLACE } && Inventory.getCount { it.id == ItemID.DODGY_NECKLACE } < 4)
            return States.HANDLE_BANK
        if (!Inventory.contains { it.id == ItemID.DODGY_NECKLACE } && !Equipment.contains { it.id == ItemID.DODGY_NECKLACE })
            return States.HANDLE_BANK
        if (Inventory.getCount(true, "Coin pouch") == 28)
            return States.OPEN_POUCH
        if (Inventory.isFull() && !Inventory.contains { "Coin pouch" in it.name })
            return States.UNKNOWN
        return States.STEAL
    }

}