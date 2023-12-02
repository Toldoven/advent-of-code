package aockt.y2015

import aockt.*
import io.github.jadarma.aockt.core.Solution
import kotlin.math.max

object Y2015D06 : Solution {

    enum class Instruction { TURN_ON, TURN_OFF, TOGGLE }

    private fun parseInput(input: String): List<Triple<Instruction, IntVec2, IntVec2>> = input.lines().map {
        val split = it.splitFromEnd(' ', limit = 4)
        Triple(
            split[0].toEnum<Instruction>(),
            split[1].toIntVec2(),
            split[3].toIntVec2()
        )
    }

    private inline fun <T> grid(init: (Int) -> T) = List(1000) { MutableList(1000, init) }

    override fun partOne(input: String) = parseInput(input)
        .fold(grid { false }) { grid, (instruction, from, through) ->
            (from.x..through.x).forEach { x ->
                (from.y..through.y).forEach { y ->
                    grid[x][y] = when (instruction) {
                        Instruction.TURN_ON -> true
                        Instruction.TURN_OFF -> false
                        Instruction.TOGGLE -> !grid[x][y]
                    }
                }
            }
            grid
        }
        .flatten()
        .count { it }

    override fun partTwo(input: String) = parseInput(input)
        .fold(grid { 0 }) { grid, (instruction, from, through) ->
            (from.x..through.x).forEach { x ->
                (from.y..through.y).forEach { y ->
                    when (instruction) {
                        Instruction.TURN_ON -> grid[x][y] += 1
                        Instruction.TURN_OFF -> grid[x][y] = max(grid[x][y] - 1, 0)
                        Instruction.TOGGLE -> grid[x][y] += 2
                    }
                }
            }
            grid
        }
        .flatten()
        .sum()
}