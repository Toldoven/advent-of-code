package year2016.day09

import framework.solution

private tailrec fun String.decompressedLength(v2: Boolean = false, start: Int = 0, acc: Long = 0L): Long {
    if (start >= this.length) return acc

    return if (this[start] == '(') {
        val firstSliceIndex = start + 1
        val secondSliceIndex = this.indexOf(')', firstSliceIndex) - 1
        val (howMany, times) = this
            .slice(firstSliceIndex..secondSliceIndex)
            .split('x')
            .map { it.toInt() }

        val value = this.slice(secondSliceIndex + 2 until secondSliceIndex + 2 + howMany)
        @Suppress("NON_TAIL_RECURSIVE_CALL") val length = if (v2) value.decompressedLength(true) else value.length.toLong()

        this.decompressedLength(v2, secondSliceIndex + 2 + howMany, acc + length * times)
    } else {
        this.decompressedLength(v2, start + 1, acc + 1)
    }
}

fun main() = solution(2016, 9, "Explosives in Cyberspace") {
    partOne {
        input.decompressedLength()
    } 
    
    partOneTest {
        "ADVENT" shouldOutput 6
        "A(1x5)BC" shouldOutput 7
        "(3x3)XYZ" shouldOutput 9
        "A(2x2)BCD(2x2)EFG" shouldOutput 11
        "(6x1)(1x3)A" shouldOutput 6
        "X(8x2)(3x3)ABCY" shouldOutput 18
    }

    partTwo {
        input.decompressedLength(true)
    }

    partTwoTest {
        "(3x3)XYZ" shouldOutput 9
        "X(8x2)(3x3)ABCY" shouldOutput 20
        "(27x12)(20x12)(13x14)(7x10)(1x12)A" shouldOutput 241920
        "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN" shouldOutput 445
    }
}