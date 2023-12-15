package year2015.day24

import framework.InputProvider
import framework.solution
import utils.combinations
import utils.product

fun main() = solution(2015, 24, "It Hangs in the Balance") {

    fun InputProvider.parseInput() = lines.map { it.toLong() }

    fun List<Long>.lowestQuantumEntanglement(groupSize: Int): Long {
        val sumTarget = this.sum() / groupSize
        return generateSequence(1) { it + 1 }
            .map { size ->
                this.combinations(size).filter { it.sum() == sumTarget }
            }
            .first { it.isNotEmpty() }
            .minOf { it.product() }
    }

    partOne {
        parseInput().lowestQuantumEntanglement(3)
    }

    partTwo {
        parseInput().lowestQuantumEntanglement(4)
    }

    val testInput = listOf(1..5, 7..11).flatten().joinToString("\n")

    partOneTest {
        testInput shouldOutput 99
    }

    partTwoTest {
        testInput shouldOutput 44
    }
}