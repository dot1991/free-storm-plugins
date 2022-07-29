package net.unethicalite.woodcutter.util

import net.runelite.api.ItemID
import net.runelite.api.Skill


enum class Tree(val tree: String) {
    TREE("Tree"),
    OAK("Oak"),
    WILLOW("Willow"),
    TEAK("Teak"),
    MAPLE_TREE("Maple tree"),
    YEW("Yew"),
    MAGIC_TREE("Magic tree");
    override fun toString(): String {
        return tree
    }
}


