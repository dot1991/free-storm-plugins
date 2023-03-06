package net.unethicalite.quickherblore.interfaces

class InventoryItems(var _id: Int, var _name: String, var _quantity: Int, var _stackable: Boolean, var _exact: Boolean, var _potion: Boolean) {

    var id = _id
    var name = _name
    var quantity = _quantity
    var stackable = _stackable
    var exact = _exact
    var potion = _potion
}