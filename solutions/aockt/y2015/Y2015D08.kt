package aockt.y2015

import io.github.jadarma.aockt.core.Solution

object Y2015D08 : Solution {

    private val partOneRegex = Regex("""\\"|\\{2}|\\x[0-9a-f]{2}""")

    override fun partOne(input: String) = input.lines().sumOf {
        val decoded = it.trim('"').replace(partOneRegex, "_")
        it.length - decoded.length
    }

    override fun partTwo(input: String) = input.lines().sumOf {
        val encoded = it.replace("\\", "\\\\").replace("\"", "\\\"")
        encoded.length + 2 - it.length
    }
}