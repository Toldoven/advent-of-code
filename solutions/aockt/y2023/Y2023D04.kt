package aockt.y2023

import io.github.jadarma.aockt.core.Solution
import kotlin.math.pow

object Y2023D04 : Solution {

    private fun parseNumbers(string: String) = string.split(' ')
        .mapNotNull { it.toIntOrNull() }
        .toSet()

    private fun parseInput(input: String) = input.lineSequence().map { line ->
        val (winning, yours) = line.substringAfter(':').split(" | ")
        parseNumbers(winning) to parseNumbers(yours)
    }

    override fun partOne(input: String) = parseInput(input)
        .map { (winning, yours) -> yours.count { winning.contains(it) } }
        .sumOf { 2.0.pow(it - 1).toInt() }

    override fun partTwo(input: String): Int {
        val allCards = parseInput(input).toList()

        val memoize = hashMapOf<Int, Int>()

        fun processCard(index: Int): Int = memoize.getOrPut(index) {
            val (yours, winning) = allCards[index]
            val cardsMatch = yours.count { winning.contains(it) }
            if (cardsMatch > 0) {
                val range = (index + 1)..(index + cardsMatch)
                range.sumOf { processCard(it) } + 1
            } else {
                1
            }
        }

        return allCards.indices.sumOf { processCard(it) }
    }
}