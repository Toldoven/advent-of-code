package aockt.y2021

import io.github.jadarma.aockt.core.Solution

object Y2021D01 : Solution {

    private fun parseInput(input: String) = input.lines().map(String::toInt)

    private fun <T: Comparable<T>> Iterable<T>.countIncreases() =
        zipWithNext().count { it.first < it.second }

    override fun partOne(input: String) = parseInput(input)
        .countIncreases()

    override fun partTwo(input: String) = parseInput(input)
        .windowed(3)
        .map { it.sum() }
        .countIncreases()
}