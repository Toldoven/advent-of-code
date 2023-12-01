package aockt.y2021

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2021, 2)
class Y2021D02Test : AdventSpec<Y2021D02>({

    val testInput = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """

    partOne {
        testInput.trimIndent() shouldOutput 150
    }

    partTwo {
        testInput.trimIndent() shouldOutput 900
    }
})