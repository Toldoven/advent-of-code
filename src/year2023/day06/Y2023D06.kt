package year2023.day06

import framework.solution
import utils.parseNumbersLong
import utils.productOf

fun main() = solution(2023, 6, "Wait For It") {

    fun countWaysToBeatRecord(time: Long, record: Long) = (0..time).count { heldFor ->
        val traveled = (time - heldFor) * heldFor
        traveled > record
    }

    partOne {
        val (timeList, recordList) = lines.map { line ->
            line.substringAfter(":").parseNumbersLong(' ')
        }
        timeList.zip(recordList).productOf { (time, record) ->
            countWaysToBeatRecord(time, record)
        }
    }

    partTwo {
        val (time, record) = lines.map { line ->
            line.substringAfter(":").filterNot { it.isWhitespace() }.toLong()
        }
        countWaysToBeatRecord(time, record)
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