package year2016.day16

import framework.InputProvider
import framework.Part
import framework.solution

fun main() = solution(2016, 16, "Dragon Checksum") {

    fun InputProvider.sequence() = generateSequence(input) { a ->
        val b = a.reversed().map {
            when (it) {
                '0' -> '1'
                '1' -> '0'
                else -> throw IllegalArgumentException("Should be either 1 or 0: $it")
            }
        }.joinToString("")
        a + "0" + b
    }

    tailrec fun String.checksum(): String {
        val result = chunked(2).joinToString("") {
            when (it) {
                "00", "11" -> "1"
                else -> "0"
            }
        }
        return if (result.length % 2 == 0) result.checksum() else result
    }

    fun InputProvider.solve(diskSize: Int) = sequence()
        .first { it.length >= diskSize }
        .take(diskSize)
        .checksum()

    partOne {
        solve(272)
    }

    partTwo {
        solve(35651584)
    }
    
    partOneTest {

        val oneStep: Part = {
            sequence().elementAt(1)
        }

        "1" with oneStep shouldOutput "100"
        "0" with oneStep shouldOutput "001"
        "11111" with oneStep shouldOutput "11111000000"
        "111100001010" with oneStep shouldOutput "1111000010100101011110000"

        val checksum: Part = {
            input.checksum()
        }

        "110010110100" with checksum shouldOutput "100"
    }
}