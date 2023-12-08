package year2023.day08

import framework.InputProvider
import framework.solution
import utils.cycle
import utils.leastCommonMultiplier
import utils.splitOnce

//import utils.SequenceExtension

enum class Instruction {
    LEFT,
    RIGHT,
}

typealias Network = Map<String, Pair<String, String>>

typealias Instructions = List<Instruction>

data class Pouch(val instructions: Instructions, val network: Network)

fun main() = solution(2023, 8, "Haunted Wasteland") {

    fun InputProvider.parseInput(): Pouch {
        val (instructionsInput, networkInput) = input.split("\n\n")
        val instructions = instructionsInput.map {
            when (it) {
                'L' -> Instruction.LEFT
                'R' -> Instruction.RIGHT
                else -> throw IllegalArgumentException("Unknown instruction: $it")
            }
        }
        val network = networkInput.lines().associate { line ->
            val (key, destination) = line.split(" = ")
            key to destination.trim('(', ')').splitOnce(", ")
        }
        return Pouch(instructions, network)
    }

    fun Pouch.stepsFromTo(from: String, to: (String) -> Boolean) =
        instructions.cycle().runningFold(from) { position, instruction ->
            val leftRight = network[position] ?: throw Exception("Unknown position: $position")
            when (instruction) {
                Instruction.LEFT -> leftRight.first
                Instruction.RIGHT -> leftRight.second
            }
        }.indexOfFirst(to)

    partOne {
        val pouch = parseInput()
        pouch.stepsFromTo("AAA") { it == "ZZZ" }
    }

    partTwo {
        val pouch = parseInput()
        pouch.network.keys
            .filter { it.endsWith("A") }
            .map { position -> pouch.stepsFromTo(position) { it.endsWith("Z") } }
            .leastCommonMultiplier()
    }

    partOneTest {
        """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent() shouldOutput 2

        """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent() shouldOutput 6
    }

    partTwoTest {
        """
            LR
        
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent() shouldOutput 6
    }

}
