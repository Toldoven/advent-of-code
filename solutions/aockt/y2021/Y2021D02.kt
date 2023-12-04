package aockt.y2021

import aockt.splitOnce
import aockt.toEnum
import io.github.jadarma.aockt.core.Solution

object Y2021D02 : Solution {

    class Submarine(var position: Int = 0, var depth: Int = 0, var aim: Int = 0) {
        val result get() = position * depth
    }

    enum class Command { FORWARD, UP, DOWN }

    private fun parseInput(input: String) = input.lines().map {
        val (command, value) = it.splitOnce(' ')
        command.toEnum<Command>() to value.toInt()
    }

    override fun partOne(input: String) = parseInput(input)
        .fold(Submarine()) { submarine, (command, value) ->
            submarine.apply {
                when (command) {
                    Command.FORWARD -> position += value
                    Command.UP -> depth -= value
                    Command.DOWN -> depth += value
                }
            }
        }.result

    override fun partTwo(input: String) = parseInput(input)
        .fold(Submarine()) { submarine, (command, value) ->
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