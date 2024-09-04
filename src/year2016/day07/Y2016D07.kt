package year2016.day07


import framework.InputProvider
import framework.solution


fun<T> Pair<List<IndexedValue<T>>, List<IndexedValue<T>>>.values() =
    first.map { it.value } to second.map { it.value }

fun main() = solution(2016, 7, "Internet Protocol Version 7") {

    fun InputProvider.parseInput() = lines.map { line ->
        line.split('[', ']')
            .withIndex()
            .partition { it.index % 2 == 0 }
            .values()
    }

    partOne {
        fun String.hasAbba() = windowed(4).any {
            it[0] != it[1] && it[0] == it[3] && it[1] == it[2]
        }

        parseInput().count { (supernetParts, hypernetParts) ->
            val firstValue = supernetParts.any { it.hasAbba() }
            val secondValue = hypernetParts.all { !it.hasAbba() }
            firstValue && secondValue
        }
    }

    partOneTest {
        """
            abba[mnop]qrst
            abcd[bddb]xyyx
            aaaa[qwer]tyui
            ioxxoj[asdfgh]zxcvbn
        """.trimIndent() shouldOutput 2
    }


    partTwo {

        fun String.findAllAba() = windowed(3).asSequence().mapNotNull { string ->
            if (string[0] != string[1] && string[0] == string[2]) string[0] to string[1] else null
        }

        fun String.hasSpecificAba(pattern: Pair<Char, Char>) = windowed(3).any { string ->
            string[0] == pattern.first && string[1] == pattern.second && string[2] == pattern.first
        }

        parseInput().count { (supernetParts, hypernetParts) ->
            supernetParts.any { supernetPart ->
                supernetPart.findAllAba().any { pattern ->
                    val reversedPattern = pattern.second to pattern.first
                    hypernetParts.any { hypernetPart ->
                        hypernetPart.hasSpecificAba(reversedPattern)
                    }
                }
            }
        }
    }

    partTwoTest {
        """
            aba[bab]xyz
            xyx[xyx]xyx
            aaa[kek]eke
            zazbz[bzb]cdb
        """.trimIndent() shouldOutput 3
    }
}