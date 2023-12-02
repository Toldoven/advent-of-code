package aockt.y2015

import io.github.jadarma.aockt.core.Solution

object Y2015D02 : Solution {

    private fun parseInput(input: String) = input.lineSequence().map { line ->
        line.split('x')
            .map(String::toInt)
            .let { Triple(it[0], it[1], it[2])}
    }

    override fun partOne(input: String) = parseInput(input).sumOf { (l, w, h) ->
        arrayOf(l * w, w * h, h * l).let { it.min() + it.sum() * 2 }
    }

    override fun partTwo(input: String) = parseInput(input).sumOf { (l, w, h) ->
        arrayOf(l + w, w + h, h + l).min() * 2 + (l * w * h)
    }
}