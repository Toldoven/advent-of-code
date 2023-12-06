package year2015.day11

import framework.InputProvider
import framework.solution

fun main() = solution(2015, 11, "Corporate Policy") {

    fun Char.next() = when (this) {
        'z' -> 'a'
        in 'a'..'z' -> this + 1
        else -> throw IllegalArgumentException("Character not supported")
    }

    tailrec fun incrementString(string: String, acc: String = ""): String {
        val newChar = string.last().next()
        val rest = string.dropLast(1)
        return if (newChar == 'a') {
            incrementString(rest, newChar + acc)
        } else {
            rest + newChar + acc
        }
    }

    val doubleLetterRegex = """([a-z])\1""".toRegex()

    fun InputProvider.passwordSequence() = generateSequence(input) { incrementString(it) }.filter { password ->
        val alphabet = password.windowed(3).any {
            it[0] + 2 == it[2] && it[1] + 1 == it[2]
        }
        val filter = password.all { it != 'i' && it != 'o' && it != 'l' }
        val doubleLetter = doubleLetterRegex.findAll(password).count() >= 2
        alphabet && filter && doubleLetter
    }

    partOne {
        passwordSequence().first()
    }

    partTwo {
        passwordSequence().elementAt(1)
    }

    partOneTest {
        "aaaaaaaa" shouldOutput "aaaaaabc"
    }

    partTwoTest {
        "aaaaaaaa" shouldOutput "aaaaabca"
    }
}