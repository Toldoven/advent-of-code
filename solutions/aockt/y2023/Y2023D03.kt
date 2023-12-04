package aockt.y2023

import aockt.product
import aockt.safeSlice
import io.github.jadarma.aockt.core.Solution

object Y2023D03 : Solution {

    private val numberRegex = Regex("""\d+""")

    override fun partOne(input: String): Int {
        val lines = input.lines()
        val lineLength = lines.first().length + 1

        return numberRegex.findAll(input).filter { match ->
            val lineIndex = match.range.first / lineLength
            val firstIndex = match.range.first % lineLength - 1
            val lastIndex = match.range.last % lineLength + 1
            val linesRange = lineIndex.minus(1)..lineIndex.plus(1)
            lines.safeSlice(linesRange)
                .joinToString("") { it.safeSlice(firstIndex..lastIndex) }
                .any { !it.isDigit() && it != '.' }
        }.sumOf { it.value.toInt() }
    }

    private fun findAdjacentNumbers(line: String, starIndex: Int): List<Int> {
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

    private val starRegex = Regex("""\*""")

    override fun partTwo(input: String): Int {
        val lines = input.lines()
        val lineLength = lines.first().length + 1
        return starRegex.findAll(input).sumOf { match ->
            val lineIndex = match.range.first / lineLength
            val starIndex = match.range.first % lineLength
            val linesRange = lineIndex.minus(1)..lineIndex.plus(1)
            lines.safeSlice(linesRange)
                .flatMap { findAdjacentNumbers(it, starIndex) }
                .takeIf { it.size == 2 }
                ?.product() ?: 0
        }
    }
}