package year2016.day14

import framework.InputProvider
import framework.solution
import utils.md5

fun main() = solution(2016, 14, "One-Time Pad") {

    fun String.containsSameCharacterInARow(howManyTimes: Int) = windowedSequence(howManyTimes)
        .find { window -> window.drop(1).all { it == window[0] } }
        ?.get(0)

    fun String.containsSameCharacterInARow(howManyTimes: Int, char: Char) = windowedSequence(howManyTimes)
        .any { window -> window.all { it == char } }

    fun String.md5Stretching(stretching: Int) = generateSequence(this) { it.md5() }.elementAt(stretching + 1)

    fun InputProvider.sequence(seed: Int, stretching: Int = 0) = generateSequence(seed) { it + 1 }.map { "$input$it".md5Stretching(stretching) }

    fun InputProvider.solve(stretching: Int = 0) = sequence(0, stretching)
        .withIndex()
        .windowed(1001) { it }
        .filter { window ->
            val first = window.first()
            val char = first.value.containsSameCharacterInARow(3) ?: return@filter false
            window.asSequence().drop(1).any {
                it.value.containsSameCharacterInARow(5, char)
            }
        }
        .map { it.first() }
        .take(64)
        .last()
        .index

    partOne {
        solve()
    }

    partOneTest {
        "abc" shouldOutput 22728
    }

    partTwo {
        solve(2016)
    }

    partTwoTest {
        "abc" shouldOutput 22551
    }
}