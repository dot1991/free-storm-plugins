package net.unethicalite.thief.util

import lombok.Getter

@Getter
enum class Stall(val normal: Int, val empty: Int, val action: String, vararg var item: Int) {
    CAKE(11730, 634, "Steal-from",1891, 2309, 1901),
    SILK(11729, 634,  "Steal-from", 950),
    TEA(635, 634,  "Steal-from", 1978),
    FRUIT(28823, 27537,  "Steal-from", -1)
}



