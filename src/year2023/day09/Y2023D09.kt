package year2023.day09

import framework.InputProvider
import framework.solution
import utils.parseNumbersLong

fun main() = solution(2023, 9, "Mirage Maintenance") {

    fun InputProvider.parseInput() = lines.map { it.parseNumbersLong(' ') }

    fun List<List<Long>>.readingDifferencesList() = map { reading ->
        generateSequence(reading) { differences ->
            differences.windowed(2).map { (previous, current) -> current - previous }
        }.takeWhile { differences ->
            differences.any { it != 0L }
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
                .reduceRight(Long::minus)
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