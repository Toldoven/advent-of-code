package year2015.day11

import framework.InputProvider
import framework.solution

fun main() = solution(2015, 11, "Corporate Policy") {

    val nextCharMap = ('a'..'z').associateWith {
        when (it) {
            'z' -> 'a'
            else -> (it.code + 1).toChar()
        }
    }

    fun Char.next() = nextCharMap[this] ?: throw IllegalArgumentException("Only lowercase chars are supported")

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
            val checkWrap = it[1] != 'a' && it[2] != 'a'
            val checkMatch = it[0].next().next() == it[2] && it[1].next() == it[2]
            checkWrap && checkMatch
        }
        val filter = password.all { it != 'i' && it != 'o' && it != 'l' }
        val doubleLetter = doubleLetterRegex.findAll(password).count() >= 2
        alphabet && filter && doubleLetter
    }

    partOne {
        passwordSequence().elementAt(0)
    }

    partTwo {
        passwordSequence().elementAt(1)
    }
}