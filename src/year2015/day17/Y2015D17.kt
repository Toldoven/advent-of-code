package year2015.day17

import framework.InputProvider
import framework.solution
import utils.powerSet

fun main() = solution(2015, 17, "No Such Thing as Too Much") {

    fun InputProvider.parseInput() = lines.map { it.toInt() }

    partOne {
        parseInput().powerSet().count { it.sum() == 150 }
    }

    partTwo {
        val allPossibleContainers = parseInput().powerSet().filter { it.sum() == 150 }.toList()
        val minimumNumberOfContainers = allPossibleContainers.minOf { it.size }
        allPossibleContainers.count { it.size == minimumNumberOfContainers }
    }
}