package year2015.day16

import framework.InputProvider
import framework.solution
import utils.splitOnce

fun main() = solution(2015, 16, "Aunt Sue") {

    val tapeOutput = """
        children: 3
        cats: 7
        samoyeds: 2
        pomeranians: 3
        akitas: 0
        vizslas: 0
        goldfish: 5
        trees: 3
        cars: 2
        perfumes: 1
    """.trimIndent()

    val tapeOutputParsed = tapeOutput.lines().associate {
        val (label, count) = it.splitOnce(": ")
        label to count.toInt()
    }

    fun InputProvider.parseInput() = lines.map { line ->
        line.substringAfter(": ").split(", ").associate {
            val (label, count) = it.splitOnce(": ")
            label to count.toInt()
        }
    }

    partOne {
        parseInput().indexOfFirst { sueData ->
            tapeOutputParsed.all { (compound, amount) ->
                val sueAmount = sueData[compound] ?: return@all true
                sueAmount == amount
            }
        } + 1
    }

    partTwo {
        parseInput().indexOfFirst { sueData ->
            tapeOutputParsed.all { (compound, amount) ->
                val sueAmount = sueData[compound] ?: return@all true
                when (compound) {
                    "cats", "trees" -> sueAmount > amount
                    "pomeranians", "goldfish" -> sueAmount < amount
                    else -> sueAmount == amount
                }
            }
        } + 1
    }
}