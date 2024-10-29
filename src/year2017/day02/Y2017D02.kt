package year2017.day02

import framework.InputProvider
import framework.solution
import utils.combinations
import utils.splitWhitespace

fun main() = solution(2017, 2, "Corruption Checksum") {

    fun InputProvider.parseInput() = lines.map { line ->
        line.splitWhitespace().map { it.toInt() }
    }

    partOne {
        parseInput().sumOf {
            it.max() - it.min()
        }
    }

    partOneTest {
        """
            5 1 9 5
            7 5 3
            2 4 6 8
        """.trimIndent() shouldOutput 18
    }

    partTwo {
        parseInput().sumOf { list ->
            val (a, b) = list.combinations(2)
                .map { it.sortedDescending() }
                .first { (a, b) -> a % b == 0 }
            a / b
        }
    }

    partTwoTest {
        """
            5 9 2 8
            9 4 7 3
            3 8 6 5
        """.trimIndent() shouldOutput 9
    }
}