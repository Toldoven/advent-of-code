package aockt.y2015

import io.github.jadarma.aockt.core.Solution

object Y2015D01 : Solution {

    private val operation = { acc: Int, char: Char ->
        when (char) {
            '(' -> acc + 1
            ')' -> acc - 1
            else -> acc
        }
    }

    override fun partOne(input: String) = input.fold(0, operation)

    override fun partTwo(input: String) = input.asSequence()
        .runningFold(0, operation)
        .indexOfFirst { it < 0 }
}

