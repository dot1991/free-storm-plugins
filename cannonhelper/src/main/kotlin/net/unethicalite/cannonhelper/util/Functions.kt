package net.unethicalite.cannonhelper.util

import net.unethicalite.api.game.Game
import net.unethicalite.api.game.Prices
import net.unethicalite.api.items.Inventory
import net.unethicalite.api.magic.Rune
import net.unethicalite.api.magic.RunePouch
import net.runelite.api.*
import net.unethicalite.cannonhelper.CannonHelperPlugin
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun Int.getPotionDoses(): Int {
        val partial: String = client.getItemComposition(this).name.substring(0, 11)
        var count = 0
        Inventory.getAll { it.name.contains(partial) }.map {
            val name: String = client.getItemComposition(it.id).name
            count += name.substring(name.indexOf("(") + 1, name.indexOf(")")).toInt()
        }
        return count
    }

    fun TileItem.itemToDrop(): Item? {
        val lootValue = if (this.id == ItemID.VORKATHS_HEAD) 75000 else Prices.getItemPrice(id) * quantity
        Inventory.getAll { it.isTradable && it.id != ItemID.TELEPORT_TO_HOUSE && it.id != id }.map {
            if(Prices.getItemPrice(it.id) * it.quantity < lootValue - 1000)
                return it
        }
        return null
    }


    fun haslowrunes(): Boolean {
        return !RunePouch.hasPouch() || RunePouch.getQuantity(Rune.CHAOS) < 50 || RunePouch.getQuantity(Rune.AIR) < 50 || RunePouch.getQuantity(Rune.LAW) < 50
    }

    fun CannonHelperPlugin.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun CannonHelperPlugin.isFiring(): Boolean {
        return client.getVarpValue(1) > 0
    }

    fun CannonHelperPlugin.getBalls(): Int {
        return client.getVarpValue(3)
    }
}