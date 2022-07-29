package net.unethicalite.woodcutter.util

import net.runelite.api.ItemID
import net.runelite.api.Skill


enum class Tree(val tree: String, val logId: Int) {
    TREE("Tree", ItemID.LOGS),
    OAK("Oak", ItemID.OAK_LOGS),
    WILLOW("Willow", ItemID.WILLOW_LOGS),
    TEAK("Teak", ItemID.TEAK_LOGS),
    MAPLE_TREE("Maple tree", ItemID.MAPLE_LOGS),
    YEW("Yew", ItemID.YEW_LOGS),
    MAGIC_TREE("Magic tree", ItemID.MAGIC_LOGS);
    override fun toString(): String {
        return tree
    }
}


