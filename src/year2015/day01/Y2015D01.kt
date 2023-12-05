package year2015.day01

import framework.solution

fun main() = solution(2015, 1, "Not Quite Lisp") {

    val operation = { acc: Int, char: Char ->
        when (char) {
            '(' -> acc + 1
            ')' -> acc - 1
            else -> acc
        }
    }

    partOne {
        input.fold(0, operation)
    }

    partTwo {
        input.asSequence()
            .runningFold(0, operation)
            .indexOfFirst { it < 0 }
    }

    partOneTest {
        listOf("(())", "()()") shouldAllOutput 0
        listOf("(((", "(()(()(", "))(((((") shouldAllOutput 3
        listOf("())", "))(") shouldAllOutput -1
        listOf(")))", ")())())") shouldAllOutput -3
    }

    partTwoTest {
        ")" shouldOutput 1
        "()())" shouldOutput 5
    }
}