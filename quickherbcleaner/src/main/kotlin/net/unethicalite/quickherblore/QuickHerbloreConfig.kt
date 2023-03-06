package net.unethicalite.quickherblore

import net.runelite.client.config.*
import net.unethicalite.quickherblore.util.*

@ConfigGroup("QuickHerbloreConfig")
interface QuickHerbloreConfig : Config {


    companion object {
        @ConfigSection(
            name = "Herblore Configuration",
            description = "",
            position = 10,
            keyName = "herbloreSection",
            closedByDefault = true
        )
        const val herbloreSection: String = "Herblore Configuration"
        @ConfigSection(
            name = "Debug Configuration",
            description = "",
            position = 100,
            keyName = "debugSection",
            closedByDefault = true
        )
        const val debugSection: String = "Debug Configuration"
    }

    @ConfigItem(
        keyName = "authkey",
        name = "Auth Key",
        description = "Auth key for plugin",
        position = 1,
        secret = true,
        title = "authKey"
    )
    @JvmDefault
    fun authKey(): String {
        return ""
    }

    @ConfigItem(
        keyName = "methodType",
        name = "Method Type",
        description = "Selects method type",
        position = 11,
        title = "methodType",
        section = herbloreSection
    )
    @JvmDefault
    fun methodType(): MethodType
    {
        return MethodType.CLEAN_HERB
    }

    @ConfigItem(
        keyName = "herbType",
        name = "Clean Herb",
        description = "Selects herb to clean",
        position = 12,
        title = "cleanHerb",
        section = herbloreSection
    )
    @JvmDefault
    fun cleanHerb(): Herb
    {
        return Herb.GUAM_LEAF
    }
    @ConfigItem(
            keyName = "fastClean",
            name = "Fast herb cleaning",
            description = "Toggle fast herb cleaning",
            position = 15,
            title = "fastClean",
            section = herbloreSection
    )
    @JvmDefault
    fun fastClean(): Boolean {
        return false
    }

    @ConfigItem(
        keyName = "startButton",
        name = "Start / Stop",
        description = "Starts / Stops the plugin",
        position = 1,
        title = "startButton"
    )
    @JvmDefault
    fun startButton(): Button {
        return Button()
    }

    @ConfigItem(
        keyName = "debugToggle",
        name = "Toggle debug",
        description = "Toggle debug in chat",
        position = 101,
        title = "debugToggle",
        section = debugSection
    )
    @JvmDefault
    fun debugToggle(): Boolean {
        return false
    }

}


