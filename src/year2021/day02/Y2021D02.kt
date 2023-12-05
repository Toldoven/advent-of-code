package year2021.day02

import framework.InputProvider
import framework.solution
import utils.splitOnce
import utils.toEnum

private class Submarine(var position: Int = 0, var depth: Int = 0, var aim: Int = 0) {
    val result get() = position * depth
}

private enum class Command { FORWARD, UP, DOWN }

fun main() = solution(2021, 2, "Dive!") {

    fun InputProvider.parseInput() = lines.map {
        val (command, value) = it.splitOnce(' ')
        command.toEnum<Command>() to value.toInt()
    }

    partOne {
        parseInput().fold(Submarine()) { submarine, (command, value) ->
            submarine.apply {
                when (command) {
                    Command.FORWARD -> position += value
                    Command.UP -> depth -= value
                    Command.DOWN -> depth += value
                }
            }
        }.result
    }

    partTwo {
        parseInput().fold(Submarine()) { submarine, (command, value) ->
            submarine.apply {
                when (command) {
                    Command.FORWARD -> {
                        position += value
                        depth += aim * value
                    }

                    Command.UP -> aim -= value
                    Command.DOWN -> aim += value
                }
            }
        }.result
    }

    val testInput = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 150
    }

    partTwoTest {
        testInput shouldOutput 900
    }
}