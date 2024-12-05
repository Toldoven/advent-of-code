package year2024.day04

import framework.InputProvider
import framework.solution
import utils.IntVec2
import utils.countOccurrences
import utils.swapRowsAndColumns
import utils.takeWhileNotNull

// !!! WARNING !!!
// I'm not proud of this
// Grid puzzles are the bane of my existence

fun main() = solution(2024, 4, "Ceres Search") {

    fun InputProvider.parseInput() = lines.map { it.toList() }

    fun<T> List<List<T>>.getAllDiagonals() =
        // Find these cells:
        // ABB
        // A..
        // A..
        (
            (0..<size).map { row -> IntVec2(0, row) } // A
        ).plus(
            (1..<get(0).size).map { col -> IntVec2(col, 0) } // B
        )
        .map { point ->
            // Move point in a diagonal
            // 0..
            // .1.
            // ..3
            generateSequence(point) { p ->
                IntVec2(p.x + 1, p.y + 1)
            }
                .map {
                    getOrNull(it.y)?.getOrNull(it.x)
                }
                // Stop at the first one out out of bounds
                // 0..
                // .1.
                // ..3
                //    X
                .takeWhileNotNull()
                .toList()
        }

    partOne {
        val grid = parseInput()

        grid // rows
            .plus(
                grid.swapRowsAndColumns() // columns
            )
            .plus(
                grid.getAllDiagonals() // diagonal
            )
            .plus(
                grid.map { it.asReversed() }.getAllDiagonals() // mirror diagonal
            )
            .let {
                it + it.map { it.asReversed() }
            }
            .sumOf { chars ->
                chars.joinToString("").countOccurrences("XMAS")
            }
    }
    
    partOneTest {
        """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent() shouldOutput 18
    }

    partTwo {
        val grid = parseInput()

        (0..<grid.size)
            .flatMap { column ->
                (0..<grid[0].size).map { row ->
                    IntVec2(row, column)
                }
            }
            .filter {
                grid[it.y][it.x] == 'A'
            }
            .count {
                runCatching {
                    val (a1, a2) = grid[it.y - 1][it.x - 1] to grid[it.y + 1][it.x + 1]
                    val (b1, b2) = grid[it.y - 1][it.x + 1] to grid[it.y + 1][it.x - 1]
                    ((a1 == 'M' && a2 == 'S') || (a1 == 'S' && a2 == 'M')) && ((b1 == 'M' && b2 == 'S') || (b1 == 'S' && b2 == 'M'))
                }.getOrDefault(false)
            }
    }

    partTwoTest {
        """
            .M.S......
            ..A..MSMS.
            .M.S.MAA..
            ..A.ASMSM.
            .M.S.M....
            ..........
            S.S.S.S.S.
            .A.A.A.A..
            M.M.M.M.M.
            ..........
        """.trimIndent() shouldOutput 9
    }
}