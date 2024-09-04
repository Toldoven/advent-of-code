package year2015.day03

import framework.solution
import utils.indexedAssociateWith

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

fun main() = solution(2015, 3, "Perfectly Spherical Houses in a Vacuum") {

    fun visitHouses(script: String, santaCount: Int = 1): Int {
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

    partOne {
        visitHouses(input, 1)
    }

    partTwo {
        visitHouses(input, 2)
    }

    partOneTest {
        ">" shouldOutput 2
        "^>v<" shouldOutput 4
        "^v^v^v^v^v" shouldOutput 2
    }

    partTwoTest {
        "^v" shouldOutput 3
        "^>v<" shouldOutput 3
        "^v^v^v^v^v" shouldOutput 11
    }
}
