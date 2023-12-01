package aockt.y2021

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2021, 1)
class Y2021D01Test : AdventSpec<Y2021D01>({

    val testInput = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent()

    partOne {
        testInput shouldOutput 7
    }

    partTwo {
        testInput shouldOutput 5
    }
})