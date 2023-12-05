package year2015.day10

import framework.InputProvider
import framework.solution

fun main() = solution(2015, 10, "Elves Look, Elves Say") {

    val regex = Regex("""(\d)\1*""")

    fun InputProvider.solution() = generateSequence(input) { string ->
        string.replace(regex) { it.value.length.toString() + it.value.first() }
    }

    partOne {
        solution().elementAt(40).length
    }

    partTwo {
        solution().elementAt(50).length
    }

    partOneTest {
        "1" shouldOutput 82350
    }

    partTwoTest {
        "1" shouldOutput 1166642
    }
}