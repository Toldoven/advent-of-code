package aockt.y2015

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2015, 4, "The Ideal Stocking Stuffer")
class Y2015D04Test : AdventSpec<Y2015D04>({
    partOne {
        "abcdef" shouldOutput 609043L
        "pqrstuv" shouldOutput 1048970L
    }

    partTwo {  }
})