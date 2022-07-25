package net.unethicalite.cannonhelper.util

import net.runelite.api.ItemID
import net.runelite.api.Skill


enum class Prayer(val type: String, vararg val ids: Int) {
    PRAYER_POTION("Prayer Potion",
        ItemID.PRAYER_POTION4,
        ItemID.PRAYER_POTION3,
        ItemID.PRAYER_POTION2,
        ItemID.PRAYER_POTION1
    ),
    SUPER_RESTORE("Super restore",
        ItemID.SUPER_RESTORE4,
        ItemID.SUPER_RESTORE3,
        ItemID.SUPER_RESTORE2,
        ItemID.SUPER_RESTORE1
    );
    override fun toString(): String {
        return type
    }
}


