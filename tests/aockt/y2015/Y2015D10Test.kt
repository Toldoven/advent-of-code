package aockt.y2015

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2015, 10, "Elves Look, Elves Say")
class Y2015D10Test : AdventSpec<Y2015D10>({
    partOne {
        "1" shouldOutput 82350
    }
    partTwo {
        "1" shouldOutput 1166642
    }
})