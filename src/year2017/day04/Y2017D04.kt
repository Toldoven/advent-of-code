package year2017.day04

import framework.solution
import utils.splitWhitespace

fun main() = solution(2017, 4, "High-Entropy Passphrases") {

    partOne {
        lines.count { line ->
            line
                .splitWhitespace()
                .groupingBy { it }
                .eachCount()
                .values
                .all { it == 1 }
        }
    }

    partOneTest {
        """
            aa bb cc dd ee
            aa bb cc dd aa
            aa bb cc dd aaa
        """.trimIndent() shouldOutput 2
    }
    
    partTwo {
        lines.count { line ->
            line
                .splitWhitespace()
                .map { it.toCharArray().sorted() }
                .groupingBy { it }
                .eachCount()
                .values
                .all { it == 1 }
        }
    }

    partTwoTest {
        """
            abcde fghij
            abcde xyz ecdab
            a ab abc abd abf abj
            iiii oiii ooii oooi oooo
            oiii ioii iioi iiio
        """.trimIndent() shouldOutput 3
    }
}