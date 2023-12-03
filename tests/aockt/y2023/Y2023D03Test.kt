package aockt.y2023

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2023, 3, "Gear Ratios")
class Y2023D03Test : AdventSpec<Y2023D03>({

    val testInput = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...$.*....
        .664.598..
    """.trimIndent()

    partOne {
        testInput shouldOutput 4361
    }

    partTwo {
        testInput shouldOutput 467835
    }
})