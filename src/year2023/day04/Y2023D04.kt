package year2023.day04

import arrow.core.MemoizedDeepRecursiveFunction
import framework.solution
import kotlin.math.pow

fun main() = solution(2023, 4, "Scratchcards") {

    fun String.parseNumbers() = split(' ').mapNotNull { it.toIntOrNull() }

    fun parseInput(input: String) = input.lineSequence().map { line ->
        val (winning, yours) = line.substringAfter(':').split(" | ")
        winning.parseNumbers() to yours.parseNumbers()
    }

    partOne {
        parseInput(input).sumOf { (winning, yours) ->
            val count = yours.count { winning.contains(it) }
            2.0.pow(count - 1).toInt()
        }
    }

    partTwo {
        val allCards = parseInput(input).toList()

        val processCard = MemoizedDeepRecursiveFunction<Int, Int> { index ->
            val (yours, winning) = allCards[index]
            val cardsMatch = yours.count { winning.contains(it) }
            if (cardsMatch > 0) {
                val range = index + 1..index + cardsMatch
                range.sumOf { callRecursive(it) } + 1
            } else {
                1
            }
        }

        allCards.indices.sumOf { processCard(it) }
    }

    val testInput = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 13
    }

    partTwoTest {
        testInput shouldOutput 30
    }
}