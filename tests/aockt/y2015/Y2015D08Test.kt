package aockt.y2015

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2015, 8)
class Y2015D08Test : AdventSpec<Y2015D08>({

    val testInput = """
        ""
        "abc"
        "aaa\"aaa"
        "\x27"
    """.trimIndent()

    partOne {
        testInput shouldOutput 12
    }

    partTwo {
        testInput shouldOutput 19
    }
})