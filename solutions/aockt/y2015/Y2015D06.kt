package aockt.y2015

import aockt.IntVec2
import aockt.splitFromEnd
import aockt.toEnum
import aockt.toIntVec2
import io.github.jadarma.aockt.core.Solution
import kotlin.math.max

typealias Grid<T> = List<MutableList<T>>

object Y2015D06 : Solution {

    enum class Instruction { TURN_ON, TURN_OFF, TOGGLE }

    private fun parseInput(input: String) = input.lines().map {
        val split = it.splitFromEnd(' ', limit = 4)
        Triple(
            split[0].toEnum<Instruction>(),
            split[1].toIntVec2(),
            split[3].toIntVec2()
        )
    }

    private inline fun <T> grid(init: (Int) -> T) = List(1000) { MutableList(1000, init) }

    private fun <T> Grid<T>.mapRange(
        from: IntVec2,
        through: IntVec2,
        action: (T) -> T,
    ): List<MutableList<T>> {
        (from.x..through.x).forEach { x ->
            (from.y..through.y).forEach { y ->
                this[x][y] = action(this[x][y])
            }
        }
        return this
    }

    override fun partOne(input: String) = parseInput(input)
        .fold(grid { false }) { grid, (instruction, from, through) ->
            grid.mapRange(from, through) { current ->
                when (instruction) {
                    Instruction.TURN_ON -> true
                    Instruction.TURN_OFF -> false
                    Instruction.TOGGLE -> !current
                }
            }
        }
        .flatten()
        .count { it }

    override fun partTwo(input: String) = parseInput(input)
        .fold(grid { 0 }) { grid, (instruction, from, through) ->
            grid.mapRange(from, through) { current ->
                when (instruction) {
                    Instruction.TURN_ON -> current + 1
                    Instruction.TURN_OFF -> max(current - 1, 0)
                    Instruction.TOGGLE -> current + 2
                }
            }
        }
        .flatten()
        .sum()
}