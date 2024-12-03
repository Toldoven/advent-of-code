package year2024.day03

import framework.InputProvider
import framework.solution

sealed class Instruction {
    data class Mul(val first: Int, val second: Int): Instruction()
    data object Do: Instruction()
    data object Dont: Instruction()
}

fun main() = solution(2024, 3, "Mull It Over") {

    val regex = Regex("""(do|don't|mul)\((?:(\d+)(?:\s*,(\d+))*)?\)""")

    fun InputProvider.parseInput() = regex.findAll(input).map {
        val keyword = it.groupValues[1]
        val arguments = it.groupValues.drop(2)
        when (keyword) {
            "do" -> {
                assert(arguments.isEmpty())
                Instruction.Do
            }
            "don't" -> {
                assert(arguments.isEmpty())
                Instruction.Dont
            }
            "mul" -> {
                assert(arguments.size == 2)
                Instruction.Mul(
                    arguments[0].toInt(),
                    arguments[1].toInt()
                )
            }
            else -> throw Exception("Unknown keyword: $keyword")
        }
    }

    partOne {
        parseInput()
            .filterIsInstance<Instruction.Mul>()
            .sumOf { it.first * it.second }
    } 
    
    partOneTest {
        """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
        """.trimIndent() shouldOutput 161
    }

    partTwo {
        parseInput().fold(true to 0) { (switch, acc), instruction ->
            when(instruction) {
                Instruction.Do -> true to acc
                Instruction.Dont -> false to acc
                is Instruction.Mul -> {
                    switch to acc + if (switch) {
                        instruction.first * instruction.second
                    } else {
                        0
                    }
                }
            }
        }.second
    }

    partTwoTest {
        """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))""" shouldOutput 48
    }
}