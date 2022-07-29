package net.unethicalite.woodcutter

import net.runelite.api.Skill
import net.runelite.client.config.*
import net.unethicalite.woodcutter.util.Tree

@ConfigGroup("WoodCutterConfig")
interface WoodCutterConfig : Config {

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
            name = "Tree Types",
            description = "",
            position = 10,
            keyName = "treeTypes",
            closedByDefault = true
        )
        const val treeType: String = "Tree Type"
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
        keyName = "tree",
        name = "Tree Type",
        description = "Choose Tree to cut",
        position = 11,
        section = treeType
    )
    @JvmDefault
    fun treeType(): Tree {
        return Tree.WILLOW
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


