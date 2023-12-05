package year2021.day01

import framework.InputProvider
import framework.solution

fun main() = solution(2021, 1, "Sonar Sweep") {

    fun InputProvider.parseInput() = lines.map { it.toInt() }

    fun <T: Comparable<T>> Iterable<T>.countIncreases() =
        zipWithNext().count { it.first < it.second }

    partOne {
        parseInput().countIncreases()
    }

    partTwo {
        parseInput()
            .windowed(3)
            .map { it.sum() }
            .countIncreases()
    }

    val testInput = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 7
    }

    partTwoTest {
        testInput shouldOutput 5
    }
}