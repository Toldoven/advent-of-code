package year2024.day06

import framework.solution
import utils.Direction
import utils.IntVec2

class GuardRoom(
    val guardInitialPos: IntVec2,
    private val obstacles: Set<IntVec2>,
    private val gridSize: IntVec2,
) {
    private fun IntVec2.inBounds() = x in 0..<gridSize.x && y in 0..<gridSize.y

    fun walkSequence(additionalObstacle: IntVec2? = null) =
        generateSequence(guardInitialPos to Direction.UP){ (currentPos, direction) ->
            val newPosition = currentPos.moveInDirection(direction)

            when {
                !newPosition.inBounds() -> null
                obstacles.contains(newPosition) || newPosition == additionalObstacle -> {
                    currentPos to direction.turnClockwise
                }
                else -> newPosition to direction
            }
        }

    companion object {
        fun parse(string: String): GuardRoom {

            val lines = string.lines()

            val gridSize = IntVec2(lines[0].length, lines.size)

            val groupByChar = lines.flatMapIndexed { rowIndex, row ->
                row.mapIndexedNotNull { colIndex , char ->
                    if (char == '#' || char == '^') char to IntVec2(colIndex, rowIndex) else null
                }
            }
                .groupBy(
                    { it.first },
                    { it.second },
                )

            val guardPosition = groupByChar['^'].let {
                require(it != null)
                require(it.size == 1)
                it.first()
            }

            val obstacles = groupByChar.getOrElse('#') { emptyList() }.toSet()

            return GuardRoom(guardPosition, obstacles, gridSize)
        }
    }
}

fun main() = solution(2024, 6, "Guard Gallivant") {

    partOne {
        GuardRoom.parse(input)
            .walkSequence()
            .distinctBy { it.first }
            .count()
    }

    partTwo {
        GuardRoom.parse(input).run {
            walkSequence()
                .distinctBy { it.first }
                .filter { it.first != guardInitialPos }
                .count { (newObstacle, _) ->
                    val set = mutableSetOf<Pair<IntVec2, Direction>>()
                    walkSequence(newObstacle).any { !set.add(it) }
                }
        }
    }

    val testInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent()
    
    partOneTest {
        testInput shouldOutput 41
    }

    partTwoTest {
        testInput shouldOutput 6
    }
}