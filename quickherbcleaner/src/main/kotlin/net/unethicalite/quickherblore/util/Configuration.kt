package net.unethicalite.quickherblore.util

import lombok.Getter
import net.runelite.api.ItemID
import net.unethicalite.quickherblore.interfaces.InventoryItems
import net.unethicalite.quickherblore.interfaces.InventorySetup
import java.util.*

@Getter
enum class Herb(val level: Int, val cleanUnnoted: Int, val grimyUnnoted: Int) {
    GUAM_LEAF(3, 249,  199),
    MARRENTILL(5, 251,  201),
    TARROMIN(11, 253,  203),
    HARRALANDER(20, 255,  205),
    RANARR_WEED(25, 257,  207),
    TOADFLAX(30, 2998,  3049),
    IRIT_LEAF(40, 259,  209),
    AVANTOE(48, 261,  211),
    KWUARM(54, 263,  213),
    SNAPDRAGON(59, 3000,  3051),
    CADANTINE(65, 265,  215),
    LANTADYME(67, 2481,  2485),
    DWARF_WEED(70, 267,  217),
    TORSTOL(75, 269,  219);

    fun getUnf(): String {
        return super.name[0].toString() + super.name.substring(1).lowercase(Locale.getDefault())
            .replace("_".toRegex(), " ") + " potion (unf)"
    }
}

@Getter
enum class Unfinished(val level: Int, val unfName: String, val id: Int, val herb: Herb, val vial: Int)
{
    GUAM_POTION(3, "Guam potion (unf)", ItemID.GUAM_POTION_UNF, Herb.GUAM_LEAF, ItemID.VIAL_OF_WATER),
    MARRENTILL_POTION(5, "Marrentill potion (unf)",  ItemID.MARRENTILL_POTION_UNF, Herb.MARRENTILL, ItemID.VIAL_OF_WATER),
    TARROMIN_POTION(12, "Tarromin potion (unf)",  ItemID.TARROMIN_POTION_UNF, Herb.TARROMIN, ItemID.VIAL_OF_WATER),
    HARRALANDER_POTION(22, "Harralander potion (unf)",  ItemID.HARRALANDER_POTION_UNF, Herb.HARRALANDER, ItemID.VIAL_OF_WATER),
    RANARR_WEED_POTION(30, "Ranarr potion (unf)",  ItemID.RANARR_POTION_UNF, Herb.RANARR_WEED, ItemID.VIAL_OF_WATER),
    TOADFLAX_POTION(34, "Toadflax potion (unf)",  ItemID.TOADFLAX_POTION_UNF, Herb.TOADFLAX, ItemID.VIAL_OF_WATER),
    IRIT_LEAF_POTION(45, "Irit potion (unf)",  ItemID.IRIT_POTION_UNF, Herb.IRIT_LEAF, ItemID.VIAL_OF_WATER),
    AVANTOE_POTION(50, "Avantoe potion (unf)",  ItemID.AVANTOE_POTION_UNF, Herb.AVANTOE, ItemID.VIAL_OF_WATER),
    KWUARM_POTION(55, "Kwuarm potion (unf)",  ItemID.KWUARM_POTION_UNF, Herb.KWUARM, ItemID.VIAL_OF_WATER),
    SNAPDRAGON_POTION(63, "Snapdragon potion (unf)",  ItemID.SNAPDRAGON_POTION_UNF, Herb.SNAPDRAGON, ItemID.VIAL_OF_WATER),
    CADANTINE_POTION(66, "Cadantine potion (unf)",  ItemID.CADANTINE_POTION_UNF, Herb.CADANTINE, ItemID.VIAL_OF_WATER),
    LANTADYME_POTION(69, "Lantadyme potion (unf)",  ItemID.LANTADYME_POTION_UNF, Herb.LANTADYME, ItemID.VIAL_OF_WATER),
    DWARF_WEED_POTION(72, "Dwarf weed potion (unf)",  ItemID.DWARF_WEED_POTION_UNF, Herb.DWARF_WEED, ItemID.VIAL_OF_WATER),
    TORSTOL_POTION(78, "Torstol potion (unf)",  ItemID.TORSTOL_POTION_UNF, Herb.TORSTOL, ItemID.VIAL_OF_WATER),
    CADANTINE_BLOOD_POTION(80, "Cadantine blood potion (unf)", ItemID.CADANTINE_BLOOD_POTION_UNF, Herb.CADANTINE, ItemID.VIAL_OF_BLOOD_22446)
}

@Getter
enum class MethodType()
{
    CLEAN_HERB
}


@Getter
enum class Secondary(val id: Int, val stackable: Boolean) {
    EYE_OF_NEWT(ItemID.EYE_OF_NEWT, false),
    UNICORN_HORN_DUST(ItemID.UNICORN_HORN_DUST, false),
    LIMPWURT_ROOT(ItemID.LIMPWURT_ROOT, false),
    SWAMP_TAR(ItemID.SWAMP_TAR, true),
    RED_SPIDERS_EGGS(ItemID.RED_SPIDERS_EGGS, false),
    CHOCOLATE_DUST(ItemID.CHOCOLATE_DUST, false),
    WHITE_BERRIES(ItemID.WHITE_BERRIES, false),
    TOADS_LEGS(ItemID.TOADS_LEGS, false),
    CRUSHED_GOATS_HORN(ItemID.GOAT_HORN_DUST, false),
    SNAPE_GRASS(ItemID.SNAPE_GRASS, false),
    MORT_MYRE_FUNGUS(ItemID.MORT_MYRE_FUNGUS, false),
    KEBBIT_TEETH_DUST(ItemID.KEBBIT_TEETH_DUST, false),
    GROUND_GORAK_CLAW(ItemID.GORAK_CLAW_POWDER, false),
    DRAGON_SCALE_DUST(ItemID.DRAGON_SCALE_DUST, false),
    WINE_OF_ZAMORAK(ItemID.WINE_OF_ZAMORAK, false),
    POTATO_CACTUS(ItemID.POTATO_CACTUS, false),
    JANGERBERRIES(ItemID.JANGERBERRIES, false),
    CRUSHED_NEST(ItemID.CRUSHED_NEST, false),
    POISON_IVY_BERRIES(ItemID.POISON_IVY_BERRIES, false),
    LAVA_DRAGON_SCALE(ItemID.LAVA_SCALE_SHARD, true),
    CRYSTAL_DUST(ItemID.CRYSTAL_DUST_23964, true),
    CRUSHED_SUPERIOR_DRAGON_BONES(ItemID.CRUSHED_SUPERIOR_DRAGON_BONES, false),
    NIHIL_DUST(ItemID.NIHIL_DUST, true),
    AMYLASE_CRYSTAL(ItemID.AMYLASE_CRYSTAL, true),
    TORSTOL(ItemID.TORSTOL, false);

    override fun toString(): String {
        return super.name.replace("_", " ").replace("1", "'").replace("2", "-").lowercase(Locale.getDefault())
    }
}

@Getter
enum class Potions(val level: Int, val setup: InventorySetup?) {
    ATTACK_POTION(3, InventorySetup(
        listOf(InventoryItems(ItemID.GUAM_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.EYE_OF_NEWT, "", 14, false, false, false)))),

    ANTIPOISON(5, InventorySetup(listOf(InventoryItems(ItemID.MARRENTILL_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.UNICORN_HORN_DUST, "", 14, false, false, false)))),

    STRENGTH_POTION(12, InventorySetup(listOf(InventoryItems(ItemID.TARROMIN_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.LIMPWURT_ROOT, "", 14, false, false, false)))),

    SERUM_207(15, InventorySetup(listOf(InventoryItems(ItemID.TARROMIN_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.ASHES, "", 14, false, false, false)))),

    GUAM_TAR(19, InventorySetup(listOf(InventoryItems(ItemID.SWAMP_TAR, "", 9999, true, false, false),
        InventoryItems(ItemID.GUAM_LEAF, "", 26, false, false, false),
        InventoryItems(ItemID.PESTLE_AND_MORTAR, "", 1, false, true, false)))),

    STAT_RESTORE_POTION(22, InventorySetup(listOf(InventoryItems(ItemID.HARRALANDER_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.RED_SPIDERS_EGGS, "", 14, false, false, false)))),

    ENERGY_POTION(26, InventorySetup(listOf(InventoryItems(ItemID.HARRALANDER_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.CHOCOLATE_DUST, "", 14, false, false, false)))),

    DEFENCE_POTION(30,InventorySetup(listOf(InventoryItems(ItemID.RANARR_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.WHITE_BERRIES, "", 14, false, false, false)))),

    MARRENTIL_TAR(31, InventorySetup(listOf(InventoryItems(ItemID.SWAMP_TAR, "", 9999, true, false, false),
        InventoryItems(ItemID.MARRENTILL, "", 26, false, false, false),
        InventoryItems(ItemID.PESTLE_AND_MORTAR, "", 1, false, true, false)))),

    AGILITY_POTION(34, InventorySetup(listOf(InventoryItems(ItemID.TOADFLAX_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.TOADS_LEGS, "", 14, false, false, false)))),

    COMBAT_POTION(36, InventorySetup(listOf(InventoryItems(ItemID.HARRALANDER_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.GOAT_HORN_DUST, "", 14, false, false, false)))),

    PRAYER_POTION(38, InventorySetup(listOf(InventoryItems(ItemID.RANARR_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.SNAPE_GRASS, "", 14, false, false, false)))),

    TARROMIN_TAR(39, InventorySetup(listOf(InventoryItems(ItemID.SWAMP_TAR, "", 9999, true, false, false),
        InventoryItems(ItemID.TARROMIN, "", 26, false, false, false),
        InventoryItems(ItemID.PESTLE_AND_MORTAR, "", 1, false, false, false)))),

    HARRALANDER_TAR(44, InventorySetup(listOf(InventoryItems(ItemID.SWAMP_TAR, "", 9999, true, false, false),
        InventoryItems(ItemID.HARRALANDER, "", 26, false, false, false),
        InventoryItems(ItemID.PESTLE_AND_MORTAR, "", 1, false, true, false)))),

    SUPER_ATTACK_POTION(45, InventorySetup(listOf(InventoryItems(ItemID.IRIT_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.EYE_OF_NEWT, "", 14, false, false, false)))),

    SUPER_ANTIPOISON(48, InventorySetup(listOf(InventoryItems(ItemID.IRIT_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.UNICORN_HORN_DUST, "", 14, false, false, false)))),

    FISHING_POTION(50, InventorySetup(listOf(InventoryItems(ItemID.AVANTOE_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.SNAPE_GRASS, "", 14, false, false, false)))),

    SUPER_ENERGY_POTION(52,InventorySetup(listOf(InventoryItems(ItemID.AVANTOE_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.MORT_MYRE_FUNGUS, "", 14, false, false, false)))),

    HUNTING_POTION(53, InventorySetup(listOf(InventoryItems(ItemID.AVANTOE_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.KEBBIT_TEETH_DUST, "", 14, false, false, false)))),

    SUPER_STRENGTH_POTION(55, InventorySetup(listOf(InventoryItems(ItemID.KWUARM_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.LIMPWURT_ROOT, "", 14, false, false, false)))),

    WEAPON_POISON(60, InventorySetup(listOf(InventoryItems(ItemID.KWUARM_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.DRAGON_SCALE_DUST, "", 14, false, false, false)))),

    SUPER_RESTORE_POTION(63, InventorySetup(listOf(InventoryItems(ItemID.SNAPDRAGON_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.RED_SPIDERS_EGGS, "", 14, false, false, false)))),

    SUPER_DEFENCE_POTION(66, InventorySetup(listOf(InventoryItems(ItemID.CADANTINE_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.WHITE_BERRIES, "", 14, false, false, false)))),

    ANTIFIRE_POTION(69, InventorySetup(listOf(InventoryItems(ItemID.LANTADYME_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.DRAGON_SCALE_DUST, "", 14, false, false, false)))), //CHECK NAME

    DIVINE_SUPER_ATTACK(70, InventorySetup(listOf(InventoryItems(ItemID.CRYSTAL_DUST_23964, "", 9999, true, false, false),
        InventoryItems(ItemID.SUPER_ATTACK4, "", 27, false, true, false)))),

    DIVINE_SUPER_STRENGTH(70, InventorySetup(listOf(InventoryItems(ItemID.CRYSTAL_DUST_23964, "", 9999, true, false, false),
        InventoryItems(ItemID.SUPER_STRENGTH4, "", 27, false, false, false)))),

    DIVINE_SUPER_DEFENCE(70, InventorySetup(listOf(InventoryItems(ItemID.CRYSTAL_DUST_23964, "", 9999, true, false, false),
        InventoryItems(ItemID.SUPER_DEFENCE4, "", 27, false, false, false)))),

    RANGING_POTION(72, InventorySetup(listOf(InventoryItems(ItemID.DWARF_WEED_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.WINE_OF_ZAMORAK, "", 14, false, false, false)))),

    DIVINE_RANGING_POTION(74, InventorySetup(listOf(InventoryItems(ItemID.CRYSTAL_DUST_23964, "", 9999, true, false, false),
        InventoryItems(ItemID.RANGING_POTION4, "", 27, false, false, false)))),

    MAGIC_POTION(76, InventorySetup(listOf(InventoryItems(ItemID.LANTADYME_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.POTATO_CACTUS, "", 14, false, false, false)))),

    STAMINA_POTION(77,InventorySetup(listOf(InventoryItems(ItemID.SUPER_ENERGY4, "", 27, false, false, false),
        InventoryItems(ItemID.AMYLASE_CRYSTAL, "", 9999, true, false, false)))),

    ZAMORAK_BREW(78,InventorySetup(listOf(InventoryItems(ItemID.TORSTOL_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.JANGERBERRIES, "", 14, false, false, false)))),

    DIVINE_MAGIC_POTION(78, InventorySetup(listOf(InventoryItems(ItemID.CRYSTAL_DUST_23964, "", 9999, true, false, false),
        InventoryItems(ItemID.MAGIC_POTION4, "", 27, false, false, false)))),

    BASTION_POTION(80, InventorySetup(listOf(InventoryItems(ItemID.CADANTINE_BLOOD_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.WINE_OF_ZAMORAK, "", 14, false, false, false)))),

    BATTLEMAGE_POTION(80, InventorySetup(listOf(InventoryItems(ItemID.CADANTINE_BLOOD_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.POTATO_CACTUS, "", 14, false, false, false)))),

    SARADOMIN_BREW(81, InventorySetup(listOf(InventoryItems(ItemID.TOADFLAX_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.CRUSHED_NEST, "", 14, false, false, false)))),

    EXTENDED_ANTIFIRE_POTION(84, InventorySetup(listOf(InventoryItems(ItemID.ANTIFIRE_POTION4, "", 27, false, false, false),
        InventoryItems(ItemID.LAVA_SCALE_SHARD, "", 9999, true, false, false)))),

    ANCIENT_BREW(85, InventorySetup(listOf(InventoryItems(ItemID.DWARF_WEED_POTION_UNF, "", 27, false, false, false),
        InventoryItems(ItemID.NIHIL_DUST, "", 9999, true, false, false)))),

    DIVINE_BASTION_POTION(86, InventorySetup(listOf(InventoryItems(ItemID.CRYSTAL_DUST_23964, "", 9999, true, false, false),
        InventoryItems(ItemID.BASTION_POTION4, "", 27, false, false, false)))),

    DIVINE_BATTLEMAGE_POTION(86, InventorySetup(listOf(InventoryItems(ItemID.CRYSTAL_DUST_23964, "", 9999, true, false, false),
        InventoryItems(ItemID.BATTLEMAGE_POTION4, "", 27, false, false, false)))),

    ANTI_VENOM(87, InventorySetup(listOf(InventoryItems(ItemID.ANTIDOTE4_5952, "", 14, false, false, false),
        InventoryItems(ItemID.ZULRAHS_SCALES, "", 9999, true, false, false)))),

    MENTAPHITE_REMEDY(88, InventorySetup(listOf(InventoryItems(ItemID.DWARF_WEED_POTION_UNF, "", 14, false, false, false),
        InventoryItems(ItemID.LILY_OF_THE_SANDS, "", 14, false, false, false)))),

    SUPER_COMBAT_POTION(90, InventorySetup(listOf(InventoryItems(ItemID.TORSTOL, "", 7, false, false, false),
        InventoryItems(ItemID.SUPER_ATTACK4, "", 7, false, false, false),
        InventoryItems(ItemID.SUPER_DEFENCE4, "", 7, false, false, false),
        InventoryItems(ItemID.SUPER_STRENGTH4, "", 7, false, false, false)))),

    SUPER_ANTIFIRE(92, InventorySetup(listOf(InventoryItems(ItemID.ANTIFIRE_POTION4, "", 14, false, false, false),
        InventoryItems(ItemID.CRUSHED_SUPERIOR_DRAGON_BONES, "", 14, false, false, false)))),

    ANTI_VENOM_PLUS(94, InventorySetup(listOf(InventoryItems(ItemID.ANTIVENOM4, "", 14, false, false, false),
        InventoryItems(ItemID.TORSTOL, "", 14, false, false, false)))),

    DIVINE_SUPER_COMBAT(97, InventorySetup(listOf(InventoryItems(ItemID.CRYSTAL_DUST_23964, "", 9999, true, false, false),
        InventoryItems(ItemID.SUPER_COMBAT_POTION4, "", 27, false, false, false)))),

    EXTENDED_SUPER_ANTIFIRE(98, InventorySetup(listOf(InventoryItems(ItemID.SUPER_ANTIFIRE_POTION4, "", 14, false, false, false),
        InventoryItems(ItemID.LAVA_SCALE_SHARD, "", 9999, true, false, false))));
}
