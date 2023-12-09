package year2023.day09

import framework.InputProvider
import framework.solution
import utils.parseNumbersInt

fun main() = solution(2023, 9, "Mirage Maintenance") {

    fun InputProvider.parseInput() = lines.map { it.parseNumbersInt(' ') }

    fun List<List<Int>>.readingDifferencesList() = map { reading ->
        generateSequence(reading) { currentDifference ->
            if (currentDifference.all { it == 0 }) {
                return@generateSequence null
            }
            currentDifference.zipWithNext().map { (previous, current) ->
                current - previous
            }
        }
    }

    partOne {
        parseInput().readingDifferencesList().sumOf { readingDifferences ->
            readingDifferences.sumOf { it.last() }
        }
    }

    partTwo {
        parseInput().readingDifferencesList().sumOf { readingDifferenceSequence ->
            readingDifferenceSequence.map { it.first() }
                .toList()
                .reversed()
                .reduce { previous, current -> current - previous }
        }
    }

    val testInput = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 114
    }

    partTwoTest {
        testInput shouldOutput 2
    }
}