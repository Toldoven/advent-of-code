package year2015.day06

import framework.InputProvider
import framework.solution
import utils.IntVec2
import utils.splitFromEnd
import utils.toEnum
import utils.toIntVec2
import kotlin.math.max

typealias Grid<T> = List<MutableList<T>>

enum class Instruction { TURN_ON, TURN_OFF, TOGGLE }

fun main() = solution(2015, 6, "Probably a Fire Hazard") {

    fun InputProvider.parseInput() = lines.map {
        val split = it.splitFromEnd(' ', limit = 4)
        Triple(
            split[0].toEnum<Instruction>(),
            split[1].toIntVec2(),
            split[3].toIntVec2()
        )
    }

    fun <T> grid(init: (Int) -> T) = List(1000) { MutableList(1000, init) }

    fun <T> Grid<T>.mapRange(
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

    partOne {
        parseInput().fold(grid { false }) { grid, (instruction, from, through) ->
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
    }

    partTwo {
        parseInput().fold(grid { 0 }) { grid, (instruction, from, through) ->
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


    partOneTest {
        "turn on 0,0 through 999,999" shouldOutput 1_000_000
        "toggle 0,0 through 999,0" shouldOutput 1000
    }

    partTwoTest {
        "toggle 0,0 through 999,999" shouldOutput 2_000_000
    }
}