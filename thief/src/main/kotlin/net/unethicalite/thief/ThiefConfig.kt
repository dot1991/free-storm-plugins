package net.unethicalite.thief

import net.runelite.api.NpcID
import net.runelite.client.config.*
import net.unethicalite.thief.util.Stall

@ConfigGroup("ThiefConfig")
interface ThiefConfig : Config {

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
            name = "Configuration",
            description = "",
            position = 10,
            keyName = "configuration",
            closedByDefault = true
        )
        const val configuration: String = "Configuration"
    }


    @Range(min = 0, max = 160)
    @ConfigItem(keyName = "sleepMin", name = "Sleep Min", description = "", position = 4, section = sleepDelays)
    @JvmDefault

    fun sleepMin(): Int {
        return 60
    }

    @Range(min = 0, max = 160)
    @ConfigItem(keyName = "sleepMax", name = "Sleep Max", description = "", position = 5, section = sleepDelays)
    @JvmDefault

    fun sleepMax(): Int {
        return 350
    }

    @Range(min = 0, max = 160)
    @ConfigItem(keyName = "sleepTarget", name = "Sleep Target", description = "", position = 6, section = sleepDelays)
    @JvmDefault

    fun sleepTarget(): Int {
        return 100
    }

    @Range(min = 0, max = 160)
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
        keyName = "stall",
        name = "Stall",
        description = "Stall to steal from",
        position = 11,
            title = "stall",
            section = configuration
    )
    @JvmDefault
    fun stall(): Stall
    {
        return Stall.TEA
    }

    @ConfigItem(
            keyName = "shouldBank",
            name = "Bank",
            description = "Enables banking at nearest bank (in api)",
            position = 12,
            title = "shouldBank",
            section = configuration
    )
    @JvmDefault
    fun shouldBank(): Boolean
    {
        return false
    }


    @ConfigItem(
            keyName = "returnCoord",
            name = "Return coordinate",
            description = "Return coordinate after moving",
            position = 13,
            title = "returnCoord",
            section = configuration
    )
    @JvmDefault
    fun returnCoord(): String {
        return ""
    }

    @ConfigItem(
            keyName = "fetchCoord",
            name = "Fetch coord",
            description = "Fetche current coordinate",
            position = 14,
            title = "fetchCoord",
            section = configuration
    )
    @JvmDefault
    fun fetchCoord(): Button {
        return Button()
    }

    @ConfigItem(
        keyName = "startButton",
        name = "Start / Stop",
        description = "Press button to start / stop plugin",
        position = 20
    )
    @JvmDefault
    fun startButton(): Button? {
        return Button()
    }

    @ConfigItem(
        keyName = "enabledDebug",
        name = "Debug",
        description = "Enable state debugging",
        position = 21
    )
    @JvmDefault
    fun enableDebugging(): Boolean {
        return false
    }

}


