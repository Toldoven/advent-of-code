package year2023.day10

import framework.InputProvider
import framework.solution
import utils.*
import utils.Direction.*

enum class Pipe(val char: Char, val direction: Set<Direction>) {
    VERTICAL('|', setOf(UP, DOWN)),
    HORIZONTAL('-', setOf(LEFT, RIGHT)),
    NORTH_EAST('L', setOf(UP, RIGHT)),
    NORTH_WEST('J', setOf(UP, LEFT)),
    SOUTH_WEST('7', setOf(DOWN, LEFT)),
    SOUTH_EAST('F', setOf(DOWN, RIGHT)),
    START('S', Direction.entries.toSet());

    companion object {
        private val charMap = entries.associateBy { it.char }

        fun fromCharOrNull(char: Char) = charMap[char]
    }
}


fun main() = solution(2023, 10, "Pipe Maze") {

    fun InputProvider.parseInput() = lines
        .map { line ->
            line.map { Pipe.fromCharOrNull(it) }
        }
        .swapRowsAndColumns()
        .toGrid()

    fun Grid<Pipe?>.tracePath(): List<IntVec2> {

        fun findConnected(point: IntVec2): List<IntVec2> {
            val currentPipe = getOrNull(point) ?: throw IllegalStateException("There is no pipe on point: $point")
            return currentPipe.direction.map { point.moveInDirection(it) }
        }

        tailrec fun tracePath(path: List<IntVec2>, target: IntVec2): List<IntVec2> {
            require(path.size >= 2) { "Starting path should already have a current and a previous position" }
            val (previous, current) = path.takeLast(2)
            val next = findConnected(current).singleOrNull { it != previous }
                ?: throw IllegalStateException("Pipe at $current doesn't have a connection")
            return if (next == target) path else tracePath(path + next, target)
        }

        val (start, _) = asIndexedSequence().first { (_, pipe) -> pipe == Pipe.START }

        val next = findConnected(start).first {
            getOrNull(it) != null && start in findConnected(it)
        }

        return tracePath(listOf(start, next), start)
    }

    partOne {
        val grid = parseInput()
        val path = grid.tracePath()
        path.size / 2
    }

    partTwo {
        val grid = parseInput()
        val path = grid.tracePath()
        val pointsInPolygonCount = grid.indices
            .filter { point -> point !in path }
            .count { point -> point.isPointInPolygon(path) }
        pointsInPolygonCount
    }

    partOneTest {
        """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF
        """.trimIndent() shouldOutput 4

        """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent() shouldOutput 8

        """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
        """.trimIndent() shouldOutput 8
    }

    partTwoTest {
        """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
        """.trimIndent() shouldOutput 4

        """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
        """.trimIndent() shouldOutput 10
    }
}