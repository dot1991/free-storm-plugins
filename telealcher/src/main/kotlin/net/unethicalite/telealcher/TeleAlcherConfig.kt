package net.unethicalite.telealcher

import net.runelite.client.config.*
import net.unethicalite.telealcher.util.Teleport

@ConfigGroup("TeleAlcherConfig")
interface TeleAlcherConfig : Config {

    companion object {
        @ConfigSection(
            name = "Sleep Delays",
            description = "",
            position = 3,
            keyName = "sleepDelays",
            closedByDefault = true
        )
        const val sleepDelays: String = "Sleep Delays"

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
        keyName = "teleport",
        name =  "Teleport",
        description = "Teleport",
        position = 12
    )
    @JvmDefault
    fun teleport(): Teleport {
        return Teleport.CAMELOT
    }

    @ConfigItem(
        keyName = "toggleAlch",
        name = "Toggle Alch",
        description = "Toggle alch",
        position = 13
    )
    @JvmDefault
    fun toggleAlch(): Boolean {
        return true
    }

    @ConfigItem(
        keyName = "alchId",
        name = "Alch ID",
        description = "Alch item id",
        position = 14
    )
    @JvmDefault
    fun alchId(): Int {
        return 995
    }

    @ConfigItem(
        keyName = "startHelper",
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


