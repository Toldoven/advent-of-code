package aockt.y2015

import aockt.md5
import io.github.jadarma.aockt.core.Solution

object Y2015D04 : Solution {

    private fun solveHash(key: String, zeros: Int) = generateSequence(1L) { it + 1 }.first { nonce ->
        (key + nonce).md5().take(zeros).all { it == '0' }
    }

    override fun partOne(input: String) = solveHash(input, 5)

    override fun partTwo(input: String) = solveHash(input, 6)
}