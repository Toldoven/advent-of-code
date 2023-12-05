package year2015.day02

import framework.InputProvider
import framework.solution

fun main() = solution(2015, 2, "I Was Told There Would Be No Math") {

    fun InputProvider.parseInput() = lineSequence.map { line ->
        line.split('x')
            .map(String::toInt)
            .let { Triple(it[0], it[1], it[2])}
    }

    partOne {
        parseInput().sumOf { (l, w, h) ->
            arrayOf(l * w, w * h, h * l).let { it.min() + it.sum() * 2 }
        }
    }

    partTwo {
        parseInput().sumOf { (l, w, h) ->
            arrayOf(l + w, w + h, h + l).min() * 2 + (l * w * h)
        }
    }

    partOneTest {
        "2x3x4" shouldOutput 58
        "1x1x10" shouldOutput 43
    }

    partTwoTest {
        "2x3x4" shouldOutput 34
        "1x1x10" shouldOutput 14
    }
}