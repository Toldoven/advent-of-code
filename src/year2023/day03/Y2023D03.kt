package year2023.day03

import framework.solution
import utils.product
import utils.safeSlice

fun main() = solution(2023, 3, "Gear Ratios") {

    partOne {
        val lineLength = lines.first().length + 1
        """\d+""".toRegex().findAll(input).filter { match ->
            val lineIndex = match.range.first / lineLength
            val firstIndex = match.range.first % lineLength - 1
            val lastIndex = match.range.last % lineLength + 1
            val linesRange = lineIndex.minus(1)..lineIndex.plus(1)
            lines.safeSlice(linesRange)
                .joinToString("") { it.safeSlice(firstIndex..lastIndex) }
                .any { !it.isDigit() && it != '.' }
        }.sumOf { it.value.toInt() }
    }

    fun findAdjacentNumbers(line: String, starIndex: Int): List<Int> {
        val right = line.substring(starIndex + 1)
            .takeWhile { it.isDigit() }
            .takeIf { it.isNotEmpty() }

        val left = line.substring(0, starIndex)
            .takeLastWhile { it.isDigit() }
            .takeIf { it.isNotEmpty() }

        val center = line[starIndex].digitToIntOrNull()?.toString()

        return if (center != null) {
            listOfNotNull(left, center, right)
                .joinToString("")
                .toInt()
                .let { listOf(it) }
        } else {
            listOfNotNull(left, right).map { it.toInt() }
        }
    }

    partTwo {
        val lineLength = lines.first().length + 1
        """\*""".toRegex().findAll(input).sumOf { match ->
            val lineIndex = match.range.first / lineLength
            val starIndex = match.range.first % lineLength
            val linesRange = lineIndex.minus(1)..lineIndex.plus(1)
            lines.safeSlice(linesRange)
                .flatMap { findAdjacentNumbers(it, starIndex) }
                .takeIf { it.size == 2 }
                ?.product() ?: 0
        }
    }

    val testInput = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598..
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 4361
    }

    partTwoTest {
        testInput shouldOutput 467835
    }
}