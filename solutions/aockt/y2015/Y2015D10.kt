package aockt.y2015

import io.github.jadarma.aockt.core.Solution

object Y2015D10 : Solution {

    private val regex = Regex("""(\d)\1*""")

    private fun solution(input: String) = generateSequence(input) { string ->
        string.replace(regex) { it.value.length.toString() + it.value.first() }
    }

    override fun partOne(input: String) = solution(input).elementAt(40).length

    override fun partTwo(input: String) = solution(input).elementAt(50).length
}