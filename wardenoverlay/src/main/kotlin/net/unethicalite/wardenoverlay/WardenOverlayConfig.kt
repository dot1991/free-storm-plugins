package net.unethicalite.wardenoverlay

import net.runelite.client.config.Config
import net.runelite.client.config.ConfigGroup
import net.runelite.client.config.ConfigItem
import net.runelite.client.config.ConfigSection
import java.awt.Color

@ConfigGroup("WardenOverlayConfig")
interface WardenOverlayConfig : Config {


    companion object {
        @ConfigSection(name = "Kephri", description = "", position = 1, keyName = "kephri", closedByDefault = true)
        const val kephri: String = "Kephri"

        @ConfigSection(name = "Ba-Ba", description = "", position = 20, keyName = "baba")
        const val baba: String = "Baba"

        @ConfigSection(name = "Warden", description = "", position = 40, keyName = "warden")
        const val warden: String = "Warden"
    }

    @ConfigItem(
        keyName = "kephrimemory",
        name = "Memory Overlay",
        description = "Memory puzzle overlay - Kephri",
        position = 2,
        section = kephri
    )
    @JvmDefault
    fun enableMemoryPuzzle(): Boolean {
        return false
    }

    @ConfigItem(
        keyName = "babafallingboulder",
        name = "Boulder Overlay",
        description = "Falling boulder overlay - Baba",
        position = 21,
        section = baba
    )
    @JvmDefault
    fun enableFallingBoulder(): Boolean {
        return false
    }

    @ConfigItem(
        keyName = "babafallingbouldercolor",
        name = "Boulder Color",
        description = "Falling boulder overlay color",
        position = 22,
        section = baba
    )
    @JvmDefault
    fun babaFallingBoulderColor(): Color {
        return Color.PINK
    }

    @ConfigItem(
        keyName = "babaslam",
        name = "Slam Overlay",
        description = "Shadow slam overlay - Baba",
        position = 23,
        section = baba
    )
    @JvmDefault
    fun enableSlamOverlay(): Boolean {
        return false
    }

    @ConfigItem(
        keyName = "babaslamcolor",
        name = "Slam Color",
        description = "Slam overlay color",
        position = 24,
        section = baba
    )
    @JvmDefault
    fun babaSlamColor(): Color {
        return Color.PINK
    }

    @ConfigItem(
        keyName = "wardencoreoverlay",
        name = "Core Overlay",
        description = "Warden core overlay - Warden",
        position = 41,
        section = warden
    )
    @JvmDefault
    fun enableWardenCore(): Boolean {
        return false
    }

    @ConfigItem(
        keyName = "wardencorecolor",
        name = "Core Color",
        description = "Core overlay color",
        position = 42,
        section = warden
    )
    @JvmDefault
    fun wardenCoreColor(): Color {
        return Color.PINK
    }

    @ConfigItem(
        keyName = "wardenflipoverlay",
        name = "Flip Overlay",
        description = "Warden flip overlay - Warden",
        position = 43
    )
    @JvmDefault
    fun enableWardenFlip(): Boolean {
        return false
    }

    @ConfigItem(
        keyName = "wardenflipcolor",
        name = "Flip Color",
        description = "Flip overlay color",
        position = 44
    )
    @JvmDefault
    fun wardenFlipColor(): Color {
        return Color.PINK
    }

    @ConfigItem(
        keyName = "wardenskulloverlay",
        name = "Warden Skull Overlay",
        description = "Warden skull overlay - Warden",
        position = 45,
        section = warden
    )
    @JvmDefault
    fun enableWardenSkull(): Boolean {
        return false
    }
    @ConfigItem(
        keyName = "wardenskullcolor",
        name = "Warden Skull Color",
        description = "Warden skull overlay color",
        position = 46,
        section = warden
    )
    @JvmDefault
    fun wardenSkullColor(): Color {
        return Color.PINK
    }

    @ConfigItem(
        keyName = "wardenlightningoverlay",
        name = "Lightning Overlay",
        description = "Warden lightning overlay - Warden",
        position = 47,
        section = warden
    )
    @JvmDefault
    fun enablelightning(): Boolean {
        return false
    }
    @ConfigItem(
        keyName = "wardenprelightningcolor",
        name = "Pre Lightning Color",
        description = "Pre lightning overlay color",
        position = 48,
        section = warden
    )
    @JvmDefault
    fun wardenPrelightningColor(): Color {
        return Color.PINK
    }
    @ConfigItem(
        keyName = "wardenlightningcolor",
        name = "Lightning Color",
        description = "lightning overlay color",
        position = 49,
        section = warden
    )
    @JvmDefault
    fun wardenlightningColor(): Color {
        return Color.PINK
    }
}


