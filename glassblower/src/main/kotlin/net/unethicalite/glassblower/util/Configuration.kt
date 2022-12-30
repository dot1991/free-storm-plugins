package net.unethicalite.glassblower.util

import lombok.Getter
import net.runelite.api.ItemID
import net.runelite.api.Skill
import net.storm.api.game.Game
import net.storm.api.game.Skills


@Getter
enum class Product(val keyValue: Int, val requiredLevel: Int) {
    HIGHEST_POSSIBLE(0, 0),
    BEER_GLASS(1, 1),
    EMPTY_CANDLE_LANTERN(2, 4),
    EMPTY_OIL_LAMP(3, 12),
    VIAL(4, 33),
    FISHBOWL(5, 42),
    UNPOWERED_ORB(6, 46),
    LANTERN_LENS(7, 49),
    LIGHT_ORB(8, 87);

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


