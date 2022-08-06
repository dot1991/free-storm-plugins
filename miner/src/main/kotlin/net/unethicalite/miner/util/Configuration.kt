package net.unethicalite.miner.util

import net.runelite.api.ItemID


enum class Rock(val item: Int, var rockId: IntArray) {
    IRON(ItemID.IRON_ORE, intArrayOf(11364, 11365));
}