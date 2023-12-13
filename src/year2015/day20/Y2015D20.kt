package year2015.day20

import framework.InputProvider
import framework.solution

fun main() = solution(2015, 20, "Infinite Elves and Infinite Houses") {

    fun solve(target: Int, giftAmount: Int, housesLimit: Int? = null): Int {
        val houseCount = target / giftAmount

        val houseList = MutableList(houseCount) { 0 }

        for (elf in 1..houseCount) {
            val elfHouseCount = housesLimit
                ?.let { elf * it + 1 }
                ?.coerceAtMost(houseCount)
                ?: houseCount
            for (number in elf until elfHouseCount step elf) {
                houseList[number] += elf * giftAmount
            }
        }

        return houseList.indexOfFirst { it >= target }
    }

    fun InputProvider.parseInput() = input.toInt()

    partOne {
        solve(parseInput(), 10)
    }

    partTwo {
        solve(parseInput(), 11, 50)
    }
}