package net.unethicalite.telealcher.util

import lombok.Getter
import net.runelite.api.Skill
import net.unethicalite.api.game.Skills
import net.unethicalite.api.magic.Magic
import net.unethicalite.api.magic.Spell
import net.unethicalite.api.magic.SpellBook

@Getter
enum class Teleport(val teleport: SpellBook.Standard, val requiredLevel: Int) {
    VARROCK(SpellBook.Standard.VARROCK_TELEPORT, 25),
    LUMBRIDGE(SpellBook.Standard.LUMBRIDGE_TELEPORT, 31),
    FALADOR(SpellBook.Standard.FALADOR_TELEPORT, 37),
    CAMELOT(SpellBook.Standard.CAMELOT_TELEPORT, 45),
    ARDOUGNE(SpellBook.Standard.ARDOUGNE_TELEPORT, 51),
    WATCH_TOWER(SpellBook.Standard.WATCHTOWER_TELEPORT, 58),
}

