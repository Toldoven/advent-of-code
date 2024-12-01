package year2024.day01

import framework.InputProvider
import framework.solution
import kotlin.math.abs

fun main() = solution(2024, 1, "Historian Hysteria") {

    val whitespaceRegex = "\\s+".toRegex()

    fun InputProvider.parseInput() = lines
        .map { line ->
            line
                .split(whitespaceRegex)
                .map { it.toInt() }
                .let { it[0] to it[1] }
        }
        .unzip()

    partOne {
        val (first, second) = parseInput()

        (first.sorted() zip second.sorted()).sumOf {
            abs(it.first - it.second)
        }
    } 
    
    partOneTest {
        """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent() shouldOutput 11
    }

    partTwo {
        val (first, second) = parseInput()

        val count = second.groupingBy { it }.eachCount()

        first.sumOf {
            it * count.getOrDefault(it, 0)
        }
    }

    partTwoTest {
        """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent() shouldOutput 31
    }
}