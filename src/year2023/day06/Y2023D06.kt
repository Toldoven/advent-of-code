package year2023.day06

import framework.solution
import utils.parseNumbersLong
import utils.productOf

fun main() = solution(2023, 6, "Wait For It") {

    partOne {
        val (timeList, distanceList) = lines.map { line ->
            line.substringAfter(":").parseNumbersLong(' ')
        }
        timeList.zip(distanceList).productOf { (time, distance) ->
            (0..time).count { heldFor ->
                val traveled = (time - heldFor) * heldFor
                traveled > distance
            }
        }
    }

    partTwo {
        val (time, distance) = lines.map { line ->
            line.substringAfter(":").filterNot { it.isWhitespace() }.toLong()
        }
        (0..time).count { heldFor ->
            val traveled = (time - heldFor) * heldFor
            traveled > distance
        }
    }

    val testInput = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 288
    }

    partTwoTest {
        testInput shouldOutput 71503
    }
}