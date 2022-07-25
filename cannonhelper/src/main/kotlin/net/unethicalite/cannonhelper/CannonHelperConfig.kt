package net.unethicalite.cannonhelper

import net.runelite.client.config.*
import net.unethicalite.cannonhelper.util.*


@ConfigGroup("CannonHelperConfig")
interface CannonHelperConfig : Config {

    companion object {
        @ConfigSection(
            name = "Sleep Delays",
            description = "",
            position = 3,
            keyName = "sleepDelays",
            closedByDefault = true
        )
        const val sleepDelays: String = "Sleep Delays"

        @ConfigSection(
            name = "Cannon Configuration",
            description = "",
            position = 10,
            keyName = "cannonSection",
            closedByDefault = true
        )
        const val cannonSection: String = "Cannon Configuration"
    }


    @Range(min = 0, max = 360)
    @ConfigItem(keyName = "sleepMin", name = "Sleep Min", description = "", position = 4, section = sleepDelays)
    @JvmDefault

    fun sleepMin(): Int {
        return 60
    }

    @Range(min = 0, max = 360)
    @ConfigItem(keyName = "sleepMax", name = "Sleep Max", description = "", position = 5, section = sleepDelays)
    @JvmDefault

    fun sleepMax(): Int {
        return 350
    }

    @Range(min = 0, max = 360)
    @ConfigItem(keyName = "sleepTarget", name = "Sleep Target", description = "", position = 6, section = sleepDelays)
    @JvmDefault

    fun sleepTarget(): Int {
        return 100
    }

    @Range(min = 0, max = 360)
    @ConfigItem(
        keyName = "sleepDeviation",
        name = "Sleep Deviation",
        description = "",
        position = 7,
        section = sleepDelays
    )
    @JvmDefault
    fun sleepDeviation(): Int {
        return 10
    }

    @ConfigItem(
        keyName = "sleepWeightedDistribution",
        name = "Sleep Weighted Distribution",
        description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
        position = 8,
        section = sleepDelays
    )
    @JvmDefault

    fun sleepWeightedDistribution(): Boolean {
        return false
    }

    @ConfigItem(
        keyName = "getTile",
        name = "Set Cannon Tile to nearest cannon",
        description = "Stand NEAREST to your cannon to get the nearest cannon location",
        position = 15
        //section = cannonSection
    )
    @JvmDefault
    fun setTile(): Button? {
        return Button()
    }

    @ConfigItem(
        keyName = "setSafeTile",
        name = "Set Safespot Tile",
        description = "Stand on your safespot and set tile. This is where it will run after it refills the cannon",
        position = 16
        //section = cannonSection
    )
    @JvmDefault
    fun setSafeTile(): Button? {
        return Button()
    }

    @ConfigItem(
        keyName = "startHelper",
        name = "Start / Stop",
        description = "Press button to start / stop plugin",
        position = 17
    )
    @JvmDefault
    fun startButton(): Button? {
        return Button()
    }

    @ConfigItem(
        keyName = "minBalls",
        name = "Minimum Cannonballs",
        description = "Minimum cannonballs before refilling",
        position = 11,
        section = cannonSection
    )
    @JvmDefault
    fun minBalls(): Int {
        return 7
    }

    @ConfigItem(
        keyName = "maxBalls",
        name = "Maximum Cannonballs",
        description = "Maximum cannonballs before refilling",
        position = 12,
        section = cannonSection
    )
    @JvmDefault
    fun maxBalls(): Int {
        return 15
    }

    @ConfigItem(
        keyName = "restorePrayer",
        name = "Restore Prayer",
        description = "Restore prayer using potions in inventory",
        position = 13,
        section = cannonSection
    )
    @JvmDefault
    fun restorePrayer(): Boolean {
        return false
    }

    @ConfigItem(
        keyName = "prayerType",
        name = "Prayer restore type",
        description = "Choose method to restore prayer",
        position = 14,
        hidden = true,
        unhide = "restorePrayer",
        section = cannonSection
    )
    @JvmDefault
    fun prayerType(): Prayer {
        return Prayer.PRAYER_POTION
    }

    @ConfigItem(
        position = 1000,
        keyName = "debugger",
        name = "Debug text in chatbox",
        description = "Debugger text",
    )
    @JvmDefault
    fun debugger(): Boolean {
        return false
    }
}


