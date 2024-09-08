package year2016.day13

import framework.InputProvider
import framework.solution
import utils.IntVec2

data class Path(val currentPosition: IntVec2, val visited: Set<IntVec2> = setOf(currentPosition))

class Maze(private val seed: Int) {
    private fun isWall(index: IntVec2): Boolean {
        val (x, y) = index

        if (x < 0 || y < 0) {
            return true
        }

        val value = (x * x + 3 * x + 2 * x * y + y + y * y) + seed
        return value.countOneBits() % 2 == 1
    }

    private fun Path.visitAll(): Sequence<Path> = currentPosition
        .adjacent4Way
        .asSequence()
        .filterNot { isWall(it) || visited.contains(it) }
        .map { Path(it, visited + it) }


    private fun bfsSequence() = generateSequence(sequenceOf(Path(IntVec2(1, 1)))) { pathList ->
        pathList.flatMap { it.visitAll() }
    }

    fun findShortestPath(
        targetPosition: IntVec2,
    ) = bfsSequence().indexOfFirst { pathList ->
        pathList.any { it.currentPosition == targetPosition }
    }

    fun distinctCoordinates(steps: Int) = bfsSequence()
        .drop(1)
        .take(steps)
        .flatten()
        .flatMap { it.visited }
        .distinct()
        .count()

    fun asString() = (0..6).joinToString("\n") { y ->
        (0..9).joinToString("") { x ->
            val index = IntVec2(x, y)
            if (isWall(index)) "#" else "."
        }
    }
}



fun main() = solution(2016, 13, "A Maze of Twisty Little Cubicles") {

    fun InputProvider.seed() = input.toInt()

    partOne {
        Maze(seed()).findShortestPath(
            IntVec2(31, 39),
        )
    }

    partOneTest {
        "10" with {
            Maze(seed()).asString()
        } shouldOutput """
            .#.####.##
            ..#..#...#
            #....##...
            ###.#.###.
            .##..#..#.
            ..##....#.
            #...##.###
        """.trimIndent()

        "10" with {
            Maze(seed()).findShortestPath(
                IntVec2(7, 4),
            )
        } shouldOutput 11
    }
    
    partTwo {
        Maze(seed()).distinctCoordinates(50)
    }
}