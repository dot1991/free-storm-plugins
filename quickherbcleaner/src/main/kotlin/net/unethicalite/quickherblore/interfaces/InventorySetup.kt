package net.unethicalite.quickherblore.interfaces

import lombok.Getter
import lombok.RequiredArgsConstructor
import net.storm.api.items.Inventory
import net.storm.api.utils.MessageUtils

@Getter
@RequiredArgsConstructor()
class InventorySetup(items: List<InventoryItems>) {

    var inventory: List<InventoryItems>? = items

    fun inventoryMatches(): Boolean {
        if (inventory == null) return true
        for (item in inventory!!)
        {
            if (!Inventory.contains(item.id) || (item.exact && Inventory.getCount(item.stackable, item.id) != item.quantity) || !item.exact && item.stackable &&  Inventory.getCount(item.stackable, item.id) < 20)
            {
                return false
            }
        }
        return true
    }

    fun anyInInv(): Boolean {
        if (inventory == null || inventory!!.isEmpty()) return false
        for (item in inventory!!)
        {
            if (item.exact && Inventory.getCount(item.stackable) {item.name in it.name} == item.quantity)
            {
                MessageUtils.addMessage("Exact: " + item.name)
                return true
            }
            if (!item.exact && Inventory.contains {item.name in it.name && !it.isNoted})
            {
                MessageUtils.addMessage("!Exact: " + item.name)
                return true
            }
            MessageUtils.addMessage("Continuing: " + item.name)
        }
        return false
    }

    fun getSize(): Int {
        var count = 0
        if (inventory == null || inventory!!.isEmpty()) return count
        for (item in inventory!!)
        {
            if (item.stackable)
                ++count
            else
                count += item.quantity
        }
        return count
    }
}