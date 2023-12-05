package year2015.day05

import framework.solution

fun main() = solution(2015, 5, "Doesn't He Have Intern-Elves For This?") {

    partOne {
        lines.count { line ->
            val vowels = line.count { arrayOf('a', 'e', 'i', 'o', 'u').contains(it) } >= 3
            val twiceInARow = line.windowed(2).find { it.chars().distinct().count() == 1L } != null
            val doesNotContain = line.windowed(2).none { arrayOf("ab", "cd", "pq", "xy").contains(it) }
            vowels && twiceInARow && doesNotContain
        }
    }

    partTwo {
        lines.count { line ->
            // Couldn't figure out how to do this without Regex
            val twoLetters = Regex("""(..).*\1""").containsMatchIn(line)
            val repeatsWithLetterInBetween = line.windowed(3).find { it[0] == it[2] } != null
            twoLetters && repeatsWithLetterInBetween
        }
    }

    partOneTest {
        """
            ugknbfddgicrmopn
            aaa
            jchzalrnumimnmhp
            haegwjzuvuyypxyu
            dvszwmarrgswjxmb
        """.trimIndent() shouldOutput 2
    }

    partTwoTest {
        """
            qjhvhtzxzqqjkmpb
            xxyxx
            uurcxstgmygtbstg
            ieodomkazucvgmuy
        """.trimIndent() shouldOutput 2
    }
}