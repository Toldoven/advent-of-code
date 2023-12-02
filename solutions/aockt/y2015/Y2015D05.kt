package aockt.y2015

import aockt.dbg
import io.github.jadarma.aockt.core.Solution

object Y2015D05 : Solution {
    override fun partOne(input: String) = input.lines().count { line ->
        val vowels = line.count { arrayOf('a', 'e', 'i', 'o', 'u').contains(it) } >= 3
        val twiceInARow = line.windowed(2).find { it.chars().distinct().count() == 1L } != null
        val doesNotContain = line.windowed(2).none { arrayOf("ab", "cd", "pq", "xy").contains(it) }
        vowels && twiceInARow && doesNotContain
    }
    override fun partTwo(input: String) = input.lines().count { line ->
        // Couldn't figure out how to do this without Regex
        val twoLetters = Regex("""(..).*\1""").containsMatchIn(line)
        val repeatsWithLetterInBetween = line.windowed(3).find { it[0] == it[2] } != null
        twoLetters && repeatsWithLetterInBetween
    }
}