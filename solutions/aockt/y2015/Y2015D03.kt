package aockt.y2015

import aockt.indexedAssociateWith
import io.github.jadarma.aockt.core.Solution

object Y2015D03 : Solution {
    class Santa(private var x: Int = 0, private var y: Int = 0) {
        fun move(direction: Char) = when (direction) {
            '^' -> y += 1
            'v' -> y -= 1
            '>' -> x += 1
            '<' -> x -= 1
            else -> {}
        }
        val currentPosition get() = x to y
    }

    private fun visitHouses(script: String, santaCount: Int = 1): Int {
        val santaPosition = MutableList(santaCount) { Santa() }
        val visited = hashSetOf(0 to 0)

        val directionToSanta = script
            .chunked(santaCount)
            .flatMap { it.indexedAssociateWith { index -> santaPosition[index] } }

        for ((direction, santa) in directionToSanta) with(santa) {
            move(direction)
            visited.add(currentPosition)
        }

        return visited.size
    }

    override fun partOne(input: String) = visitHouses(input, 1)

    override fun partTwo(input: String) = visitHouses(input, 2)
}