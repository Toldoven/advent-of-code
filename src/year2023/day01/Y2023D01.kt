package year2023.day01

import framework.solution

fun main() = solution(2023, 1, "Trebuchet?!") {

    fun Iterable<String>.sumOfFirstAndLastDigit() = sumOf { line ->
        val firstDigit = line.find { it.isDigit() }?.digitToInt() ?: 0
        val lastDigit = line.findLast { it.isDigit() }?.digitToInt() ?: 0
        firstDigit * 10 + lastDigit
    }

    partOne {
        lines.sumOfFirstAndLastDigit()
    }

    val numberMap = listOf(
        "zero", "one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine",
    ).mapIndexed { index, key -> key to "$key$index$key"}

    partTwo {
        lines.map {
            numberMap.fold(it) { line, (pattern, replaceWith) ->
                line.replace(pattern, replaceWith)
            }
        }.sumOfFirstAndLastDigit()
    }

    partOneTest {
        """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent() shouldOutput 142
    }

    partTwoTest {
        """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent() shouldOutput 281
    }
}