package year2015.day19

import framework.InputProvider
import framework.solution
import utils.letFirst
import utils.splitOnce

typealias Instructions = List<Pair<String, String>>

fun main() = solution(2015, 19, "Medicine for Rudolph") {

    fun InputProvider.parseInput(): Pair<Instructions, String> = input.splitOnce("\n\n").letFirst {
        it.lines().map { line ->
            val (instruction, result) = line.split(" => ")
            instruction to result
        }
    }

    partOne {
        val (instructions, molecule) = parseInput().letFirst {
            it.groupBy(
                { (key, _) -> key.toRegex() },
                { (_, value) -> value }
            )
        }

        instructions.flatMap { (instruction, resultList) ->
            resultList.flatMap { result ->
                instruction.findAll(molecule).map {
                    molecule.replaceRange(it.range, result)
                }
            }
        }.distinct().count()
    }

    partTwo {
        val (instructions, molecule) = parseInput().letFirst { instructions ->
            instructions.map { it.first to it.second.toRegex() }
        }

        tailrec fun findStepsToProduceMolecule(): Int {
            // Start sequence with the desired molecule and apply instructions in reverse
            val sequence = generateSequence(molecule) { molecule ->
                val allPossibleInstructions = instructions.flatMap { (instruction, result) ->
                    result.findAll(molecule).map { it to instruction }
                }
                if (allPossibleInstructions.isEmpty()) {
                    return@generateSequence null // Exit the sequence if there are no possible instructions
                }
                val (match, instruction) = allPossibleInstructions.random() // Execute a random instruction
                molecule.replaceRange(match.range, instruction)
            }
            val result = sequence.withIndex().last()
            // Recursively try again if weren't able to solve
            return if (result.value == "e") result.index else findStepsToProduceMolecule()
        }

        findStepsToProduceMolecule()
    }

    partOneTest {
        """
            H => HO
            H => OH
            O => HH

            HOH
        """.trimIndent() shouldOutput 4

        """
            H => HO
            H => OH
            O => HH

            HOHOHO
        """.trimIndent() shouldOutput 7
    }

    partTwoTest {
        """
            e => H
            e => O
            H => HO
            H => OH
            O => HH
            
            HOH
        """.trimIndent() shouldOutput 3

        """
            e => H
            e => O
            H => HO
            H => OH
            O => HH
            
            HOHOHO
        """.trimIndent() shouldOutput 6
    }
}