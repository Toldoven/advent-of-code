package aockt.y2023

import aockt.*
import io.github.jadarma.aockt.core.Solution

object Y2023D02 : Solution {

    private enum class Color { RED, GREEN, BLUE }

    private fun parseInput(input: String) = input.lineSequence().map { line ->
        val (first, second) = line.splitOnce(": ")
        val gameId = first.substringAfter(" ").toInt()
        val record = second.split("; ").map { draw ->
            draw.split(", ")
                .map { it.splitOnce(" ") }
                .associate { it.second.toEnum<Color>() to it.first.toInt() }
        }
        gameId to record
    }

    override fun partOne(input: String) = parseInput(input).filter { (_, record) ->
        record.flatten().all { (color, count) ->
            when (color) {
                Color.RED -> count <= 12
                Color.GREEN -> count <= 13
                Color.BLUE -> count <= 14
            }
        }
    }.sumOf { it.first }

    override fun partTwo(input: String) = parseInput(input).sumOf { (_, record) ->
        Color.entries.productOf { color ->
            record.maxOf { it.getOrDefault(color, 0) }
        }
    }

}