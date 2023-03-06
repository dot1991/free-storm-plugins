package net.unethicalite.quickherblore.util


import net.runelite.api.Client
import net.storm.api.items.Inventory
import net.unethicalite.quickherblore.QuickHerblorePlugin
import net.unethicalite.quickherblore.States
import javax.inject.Inject
class Functions {
    @Inject
    lateinit var client: Client
    fun QuickHerblorePlugin.getState(): States
    {
        if (chinBreakHandler.shouldBreak(this))
            return States.HANDLE_BREAK

        when (config.methodType())
        {
            MethodType.CLEAN_HERB -> {
                if (!Inventory.contains(config.cleanHerb().grimyUnnoted))
                    return States.BANKING
                return States.CLEAN_HERBS
            }
        }

        return States.IDLE
    }
}