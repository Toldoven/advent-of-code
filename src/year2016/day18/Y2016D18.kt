package year2016.day18

import framework.InputProvider
import framework.Part
import framework.solution


enum class Tile {
    TRAP, SAFE;

    override fun toString(): String = when(this) {
        TRAP -> "^"
        SAFE -> "."
    }

    companion object {
        fun fromChar(char: Char) = when(char) {
            '^' -> TRAP
            '.' -> SAFE
            else -> throw IllegalArgumentException("Unknown tile: $char")
        }
    }
}

fun main() = solution(2016, 18, "Like a Rogue") {

    fun InputProvider.parseInput() = input.map { Tile.fromChar(it)}

    fun Sequence<List<Tile>>.countSafe() = flatten().count { it == Tile.SAFE }

    fun tileSequence(firstRow: List<Tile>) = generateSequence(firstRow) { row ->
        List(row.size) { index ->
            val (left, center, right) = ((index - 1)..(index + 1)).map { row.getOrNull(it) }

            val isTrap =
                (left == Tile.TRAP && center == Tile.TRAP && right != Tile.TRAP) || // Left & center traps, right not
                (center == Tile.TRAP && right == Tile.TRAP && left != Tile.TRAP) || // Center & right traps, left not
                (left == Tile.TRAP && right != Tile.TRAP) || // Only left trap
                (right == Tile.TRAP && left != Tile.TRAP) // Only right trap

            if (isTrap) Tile.TRAP else Tile.SAFE
        }
    }

    partOne {
        tileSequence(parseInput()).take(40).countSafe()
    }

    partTwo {
        tileSequence(parseInput()).take(400000).countSafe()
    }
    
    partOneTest {
        val stringSteps: (Int) -> Part = { steps ->
            {
                tileSequence(parseInput()).take(steps).joinToString("\n") {
                    it.joinToString("")
                }
            }
        }

        "..^^." with stringSteps(3) shouldOutput """
            ..^^.
            .^^^^
            ^^..^
        """.trimIndent()

        ".^^.^.^^^^" with stringSteps(10) shouldOutput """
            .^^.^.^^^^
            ^^^...^..^
            ^.^^.^.^^.
            ..^^...^^^
            .^^^^.^^.^
            ^^..^.^^..
            ^^^^..^^^.
            ^..^^^^.^^
            .^^^..^.^^
            ^^.^^^..^^
        """.trimIndent()

        ".^^.^.^^^^" with {
            tileSequence(parseInput()).take(10).countSafe()
        } shouldOutput 38
    }
}