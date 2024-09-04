package year2016.day05

import framework.InputProvider
import framework.solution
import utils.md5

import java.math.BigInteger
import java.security.MessageDigest

fun main() = solution(2016, 5, "How About a Nice Game of Chess?") {

    fun InputProvider.md5Sequence() = generateSequence(0) { it + 1 }
        .map { index -> "$input$index".md5() }
        .filter { it.startsWith("00000") }

    partOne {
        md5Sequence()
            .take(8)
            .map { it[5] }
            .joinToString("")
    }

    partOneTest {
        "abc" shouldOutput "18f47a30"
    }

    partTwo {
        md5Sequence()
            .mapNotNull { md5 ->
                val position = md5[5].digitToIntOrNull()?.takeIf { it in 0..7 } ?: return@mapNotNull null
                val value = md5[6]
                position to value
            }
            .distinctBy { (index, _) -> index  }
            .take(8)
            .sortedBy { (index, _) -> index }
            .map { (_, value) -> value }
            .joinToString("")
    }

    partTwoTest {
        "abc" shouldOutput "05ace8e3"
    }
}