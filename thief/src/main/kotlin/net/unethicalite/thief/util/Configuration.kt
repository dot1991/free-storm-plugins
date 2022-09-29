package net.unethicalite.thief.util

import lombok.Getter

@Getter
enum class Stall(val normal: Int, val empty: Int, val item: Int, val action: String) {
    TEA(635, 634, 1978, "Steal-from"),
    FRUIT(28823, 27537, -1, "Steal-from")
}



