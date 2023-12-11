package year2023.day11

import framework.InputProvider
import framework.solution
import utils.*

fun main() = solution(2023, 11, "Cosmic Expansion") {

    fun InputProvider.parseInput() = lines.map { it.toList() }.convertRowToColumn().toGrid()

    fun Grid<Char>.sumOfDistances(expandBy: Long): Long {

        val isColumnEmptyList = columns().map { column -> column.all { it == '.' } }.toList()
        val isRowEmptyList = rows().map { row -> row.all { it == '.' } }.toList()

        fun IntVec2.expand(): LongVec2 {
            val emptyColumnsCount = isColumnEmptyList.take(x).count { it }
            val emptyRowsCount = isRowEmptyList.take(y).count { it }
            val x = x + emptyColumnsCount * expandBy.minus(1)
            val y = y + emptyRowsCount * expandBy.minus(1)
            return LongVec2(x, y)
        }

        return asIndexedSequence()
            .filter { (_, data) -> data == '#' }
            .map { (index, _) -> index.expand() }
            .toList()
            .combinations(2)
            .sumOf { (galaxyA, galaxyB) ->
                galaxyA.manhattanDistanceTo(galaxyB)
            }
    }

    partOne {
        parseInput().sumOfDistances(2)
    }

    partTwo {
        parseInput().sumOfDistances(1_000_000)
    }

    val testInput = """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 374
    }

    partTwoTest {

        testInput with {
            parseInput().sumOfDistances(10)
        } shouldOutput 1030

        testInput with {
            parseInput().sumOfDistances(100)
        } shouldOutput 8410
    }
}