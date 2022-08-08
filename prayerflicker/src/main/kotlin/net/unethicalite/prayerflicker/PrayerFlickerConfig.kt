package net.unethicalite.prayerflicker

import net.runelite.client.config.*

@ConfigGroup("PrayerFlickerConfig")
interface PrayerFlickerConfig : Config {

    companion object
    {
        @ConfigSection(
            name = "Sleep Delays",
            description = "",
            position = 2,
            keyName = "sleepDelays",
            closedByDefault = true
        )
        const val sleepDelays: String = "Sleep Delays"

        @ConfigSection(
            name = "Configuration",
            description = "",
            position = 20,
            keyName = "configuration",
            closedByDefault = false
        )
        const val configuration: String = "Configuration"
    }


    @Range(min = 0, max = 160)
    @ConfigItem(keyName = "sleepMin", name = "Sleep Min", description = "", position = 3, section = sleepDelays)
    @JvmDefault

    fun sleepMin(): Int {
        return 60
    }

    @Range(min = 0, max = 160)
    @ConfigItem(keyName = "sleepMax", name = "Sleep Max", description = "", position = 4, section = sleepDelays)
    @JvmDefault

    fun sleepMax(): Int {
        return 350
    }

    @Range(min = 0, max = 160)
    @ConfigItem(keyName = "sleepTarget", name = "Sleep Target", description = "", position = 5, section = sleepDelays)
    @JvmDefault

    fun sleepTarget(): Int {
        return 100
    }

    @Range(min = 0, max = 160)
    @ConfigItem(
        keyName = "sleepDeviation",
        name = "Sleep Deviation",
        description = "",
        position = 6,
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
        position = 7,
        section = sleepDelays
    )
    @JvmDefault
    fun sleepWeightedDistribution(): Boolean {
        return false
    }


    @ConfigItem(
        keyName = "toggleKeyBind",
        name = "Start/Stop hotkey",
        description = "Hotkey to start/stop the prayer flicker",
        position = 21,
        section = configuration
    )
    @JvmDefault
    fun toggleKeyBind(): Keybind? {
        return Keybind.NOT_SET
    }

    @ConfigItem(
        keyName = "useBreaks",
        name = "Use Break handler",
        description = "Enables the use of chin break handler - The only purpose of this is to stop tabs from switching on start",
        position = 22,
        section = configuration
    )
    @JvmDefault
    fun enableBreaks(): Boolean {
        return false
    }

}


