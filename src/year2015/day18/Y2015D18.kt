package year2015.day18

import framework.InputProvider
import framework.solution
import utils.Grid
import utils.IntVec2
import utils.convertRowToColumn
import utils.toGrid

fun main() = solution(2015, 18, "Like a GIF For Your Yard") {

    fun <T> Grid<T>.isCorner(index: IntVec2) = listOf(
        index.x == 0,
        index.y == 0,
        index.x == size.x - 1,
        index.y == size.y - 1,
    ).count { it } == 2

    fun Grid<Boolean>.processCell(index: IntVec2, value: Boolean): Boolean {
        val neighbors = getNeighbors(index).count { it }
        return when (value) {
            true -> when (neighbors) {
                in 2..3 -> true
                else -> false
            }

            false -> when (neighbors) {
                3 -> true
                else -> false
            }
        }
    }

    fun InputProvider.parseInput() = lines.map { line ->
        line.toList().map {
            when (it) {
                '#' -> true
                else -> false
            }
        }
    }.convertRowToColumn().toGrid()

    partOne {
        val sequence = generateSequence(parseInput()) { grid ->
            grid.mapIndexed { index, value ->
                grid.processCell(index, value)
            }
        }
        sequence.elementAt(100).cellSequence().count { it }
    }

    partTwo {
        val inputGrid = parseInput().let { grid ->
            grid.mapIndexed { index, value ->
                if (grid.isCorner(index)) {
                    true
                } else {
                    value
                }
            }
        }
        val sequence = generateSequence(inputGrid) { grid ->
            grid.mapIndexed { index, value ->
                if (grid.isCorner(index)) {
                    true
                } else {
                    grid.processCell(index, value)
                }
            }
        }
        sequence.elementAt(100).cellSequence().count { it }
    }


    val testInput = """
        .#.#.#
        ...##.
        #....#
        ..#...
        #.#..#
        ####..
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 4
    }

    partTwoTest {
        testInput shouldOutput 7
    }
}