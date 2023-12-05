package year2023.day02

import utils.flatten
import framework.solution
import utils.productOf
import utils.splitOnce
import utils.toEnum

private enum class Color { RED, GREEN, BLUE }

fun main() = solution(2023, 2, "Cube Conundrum") {

    fun parseInput(input: String) = input.lineSequence().map { line ->
        val (first, second) = line.splitOnce(": ")
        val gameId = first.substringAfter(" ").toInt()
        val record = second.split("; ").map { draw ->
            draw.split(", ")
                .map { it.splitOnce(" ") }
                .associate { it.second.toEnum<Color>() to it.first.toInt() }
        }
        gameId to record
    }

    partOne {
        parseInput(input).filter { (_, record) ->
            record.flatten().all { (color, count) ->
                when (color) {
                    Color.RED -> count <= 12
                    Color.GREEN -> count <= 13
                    Color.BLUE -> count <= 14
                }
            }
        }.sumOf { it.first }
    }

    partTwo {
        parseInput(input).sumOf { (_, record) ->
            Color.entries.productOf { color ->
                record.maxOf { it.getOrDefault(color, 0) }
            }
        }
    }

    val testInput = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 8
    }

    partTwoTest {
        testInput shouldOutput 2286
    }
}