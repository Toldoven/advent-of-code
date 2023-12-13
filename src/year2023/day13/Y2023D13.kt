package year2023.day13

import framework.InputProvider
import framework.solution
import utils.Grid
import utils.convertRowToColumn
import utils.toGrid

private data class ComparisonResult(val doesMatch: Boolean, val doesMatchWithSmudge: Boolean)

fun main() = solution(2023, 13, "Point of Incidence") {

    fun InputProvider.parseInput() = input.split("\n\n").map {
        it.lines().map { line -> line.toList() }.convertRowToColumn().toGrid()
    }

    fun comparePair(pair: Pair<List<Char>, List<Char>>): ComparisonResult {
        require(pair.first.size == pair.second.size) { "Lists should have identical size" }
        val totalCharacters = pair.first.size
        val countCharacterMatch = pair.first.zip(pair.second).count { it.first == it.second }
        // If the number of matching characters is only one off — we have a smudge
        val doesMatchWithSmudge = countCharacterMatch == totalCharacters - 1
        val doesMatchCompletely = countCharacterMatch == totalCharacters
        val doesMatch = doesMatchWithSmudge || doesMatchCompletely
        return ComparisonResult(doesMatch, doesMatchWithSmudge)
    }

    fun List<List<Char>>.findMirrored(desiredSmudgeCount: Int) = zipWithNext()
        .withIndex()
        .filter { (_, pair) -> comparePair(pair).doesMatch }
        .map { (index, _) -> index }
        .singleOrNull { index ->
            val firstRange = index downTo 0
            val secondRange = index.plus(1)..this.size.minus(1)
            val results = firstRange.zip(secondRange).map { (firstIndex, secondIndex) ->
                comparePair(this[firstIndex] to this[secondIndex])
            }
            val matchesWithSmudge = results.count { it.doesMatchWithSmudge }
            val doesAllMatch = results.all { it.doesMatch }
            doesAllMatch && matchesWithSmudge == desiredSmudgeCount
        }


    fun Grid<Char>.findMirrored(desiredSmudgeCount: Int): Long {
        val rows = rows().toList().findMirrored(desiredSmudgeCount)?.plus(1) ?: 0
        val columns = columns().toList().findMirrored(desiredSmudgeCount)?.plus(1) ?: 0
        return columns + rows * 100L
    }

    partOne {
        parseInput().sumOf { it.findMirrored(0) }
    }

    partTwo {
        parseInput().sumOf { it.findMirrored(1) }
    }

    val testInput = """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.

        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#        
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 405
    }

    partTwoTest {
        testInput shouldOutput 400
    }
}