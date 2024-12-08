package year2024.day08

import framework.InputProvider
import framework.solution
import utils.IntVec2
import utils.combinations

fun main() = solution(2024, 8, "Resonant Collinearity") {

    fun InputProvider.parseInput(): Pair<IntVec2, Map<Char, List<IntVec2>>>  {

        val gridSize = IntVec2(lines[0].length, lines.size)

        val charMap = lines.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, char ->
                if (char != '.') char to IntVec2(colIndex, rowIndex) else null
            }
        }
            .groupBy(
                { it.first },
                { it.second },
            )

        return gridSize to charMap
    }

    fun InputProvider.solve(part2: Boolean = false): Any {
        val (gridSize, charMap) = parseInput()

        fun IntVec2.isInBounds() = x in 0..<gridSize.x && y in 0..<gridSize.y

        return charMap.flatMap { (_, list) ->
            list.combinations(2).flatMap { (first, second) ->
                val difference = first - second

                generateSequence(listOf(first, second)) { (firstCurrent, secondCurrent) ->
                    listOf(firstCurrent + difference, secondCurrent - difference).takeIf { both ->
                        both.any { it.isInBounds() }
                    }
                }
                    .let {
                        if (part2) it else it.drop(1).take(1)
                    }
                    .flatten()
                    .toList()
            }
        }
            .filter { it.isInBounds() }
            .distinctBy { it }
            .count()
    }

    partOne {
        solve()
    }
    
    partTwo {
        solve(true)
    }

    val testInput = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 14
    }

    partTwoTest {
        testInput shouldOutput 34
    }
}