package net.unethicalite.gemcutter.util

import lombok.Getter
import net.runelite.api.ItemID
import net.runelite.api.Skill
import net.storm.api.game.Game
import net.storm.api.game.Skills


@Getter
enum class Product(val itemID: Int, val requiredLevel: Int) {
    OPAL(ItemID.UNCUT_OPAL,1),
    JADE(ItemID.UNCUT_JADE,13),
    RED_TOPAZ(ItemID.UNCUT_RED_TOPAZ,16),
    SAPPHIRE(ItemID.UNCUT_SAPPHIRE,20),
    EMERALD(ItemID.UNCUT_EMERALD,27),
    RUBY(ItemID.UNCUT_RUBY,34),
    DIAMOND(ItemID.UNCUT_DIAMOND,43),
    DRAGONSTONE(ItemID.UNCUT_ONYX,55),
    ONYX(ItemID.UNCUT_OPAL,67);

    companion object {
        fun getHighest(): Product?
        {
            val currentLevel: Int = Skills.getLevel(Skill.CRAFTING)
            for (Product in Product.values().reversedArray())
            {
                if (currentLevel >= Product.requiredLevel)
                {
                    return Product
                }
            }
            return null
        }
    }
}


