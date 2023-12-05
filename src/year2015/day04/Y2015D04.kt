package year2015.day04

import framework.solution
import utils.md5

fun main() = solution(2015, 4, "The Ideal Stocking Stuffer") {

    fun solveHash(key: String, zeros: Int) = generateSequence(1L) { it + 1 }.first { nonce ->
        (key + nonce).md5().take(zeros).all { it == '0' }
    }

    partOne {
        solveHash(input, 5)
    }

    partTwo {
        solveHash(input, 6)
    }

    partOneTest {
        "abcdef" shouldOutput 609043L
        "pqrstuv" shouldOutput 1048970L
    }
}