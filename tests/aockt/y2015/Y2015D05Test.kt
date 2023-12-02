package aockt.y2015

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2015, 5, "Doesn't He Have Intern-Elves For This?")
class Y2015D05Test : AdventSpec<Y2015D05>({
    partOne {
        """
            ugknbfddgicrmopn
            aaa
            jchzalrnumimnmhp
            haegwjzuvuyypxyu
            dvszwmarrgswjxmb
        """.trimIndent() shouldOutput 2
    }

    partTwo {
        """
            qjhvhtzxzqqjkmpb
            xxyxx
            uurcxstgmygtbstg
            ieodomkazucvgmuy
        """.trimIndent() shouldOutput 2
    }
})