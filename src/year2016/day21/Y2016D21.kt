package year2016.day21

import framework.InputProvider
import framework.Part
import framework.solution

enum class RotateDirection { LEFT, RIGHT;
    val reverse get() = when (this) {
        LEFT -> RIGHT
        RIGHT -> LEFT
    }
}

fun String.rotate(direction: RotateDirection, steps: Int): String {
    val stepsMod = steps % length
    return when (direction) {
        RotateDirection.LEFT -> {
            substring(stepsMod) + substring(0..<stepsMod)
        }

        RotateDirection.RIGHT -> {
            substring(length - stepsMod) + substring(0..<(length - stepsMod))
        }
    }
}

sealed class Operation {
    abstract fun applyToString(string: String): String

    open fun applyToStringReverse(string: String) = applyToString(string)

    data class SwapPosition(val first: Int, val second: Int): Operation() {
        override fun applyToString(string: String) = buildString(string.length) {
            append(string)
            val temp = string[first]
            set(first, get(second))
            set(second, temp)
        }
    }

    data class SwapLetter(val first: Char, val second: Char): Operation() {
        override fun applyToString(string: String) = string.map {
            when (it) {
                first -> second
                second -> first
                else -> it
            }
        }.joinToString("")
    }

    data class Rotate(val direction: RotateDirection, val steps: Int): Operation() {
        override fun applyToString(string: String) = string.rotate(direction, steps)
        override fun applyToStringReverse(string: String) = string.rotate(direction.reverse, steps)
    }

    data class RotateBasedOnPosition(val letter: Char): Operation() {
        override fun applyToString(string: String): String {
            val index = string.indexOf(letter)
            val additionalIndex = if (index >= 4) 1 else 0
            val steps = 1 + index + additionalIndex
            return string.rotate(RotateDirection.RIGHT, steps)
        }

        override fun applyToStringReverse(string: String): String {

            val steps = string.indices.find { steps ->
                val rotateLeft = string.rotate(RotateDirection.LEFT, steps)
                applyToString(rotateLeft) == string
            } ?: throw IllegalStateException("Can't find a reverse operation")

            return string.rotate(RotateDirection.LEFT, steps)
        }
    }

    data class Reverse(val range: IntRange): Operation() {
        override fun applyToString(string: String) = buildString(string.length) {
            appendRange(string, 0, range.first)
            append(
                string.substring(range).reversed()
            )
            appendRange(string, range.last + 1, string.length)
        }
    }

    data class Move(val first: Int, val second: Int): Operation() {
        override fun applyToString(string: String): String {
            val mutableList = string.toMutableList()
            val char = mutableList.removeAt(first)
            mutableList.add(second, char)
            return mutableList.joinToString("")
        }

        override fun applyToStringReverse(string: String) = Move(second, first).applyToString(string)
    }

    companion object {
        fun fromString(string: String): Operation {
            val split = string.split(' ')
            return when (val operation = split[0]) {
                "swap" -> when(val swap = split[1]) {
                    "position" -> SwapPosition(split[2].toInt(), split[5].toInt())
                    "letter" -> SwapLetter(split[2].first(), split[5].first())
                    else -> throw IllegalArgumentException("Unknown swap operation: $swap")
                }
                "reverse" -> Reverse(split[2].toInt()..split[4].toInt())
                "rotate" -> when(val rotate = split[1]) {
                    "based" -> RotateBasedOnPosition(split[6].first())
                    "left" -> Rotate(RotateDirection.LEFT, split[2].toInt())
                    "right" -> Rotate(RotateDirection.RIGHT, split[2].toInt())
                    else -> throw IllegalArgumentException("Unknown rotate operation: $rotate")
                }
                "move" -> Move(split[2].toInt(), split[5].toInt())
                else -> throw IllegalArgumentException("Unknown operation: $operation")
            }
        }
    }
}

fun main() = solution(2016, 21, "Scrambled Letters and Hash") {

    fun InputProvider.parseInput() = lines.map { Operation.fromString(it) }

    partOne {
        parseInput().fold("abcdefgh") { string, operation ->
            operation.applyToString(string)
        }
    }

    partTwo {
        parseInput().asReversed().fold("fbgdceah") { string, operation ->
            val result = operation.applyToStringReverse(string)
            assert(string == operation.applyToString(result))
            result
        }
    }

    val testInstructions = """
        swap position 4 with position 0
        swap letter d with letter b
        reverse positions 0 through 4
        rotate left 1 step
        move position 1 to position 4
        move position 3 to position 0
        rotate based on position of letter b
        rotate based on position of letter d
    """.trimIndent()

    partOneTest {
        val runningFold: Part = {
            parseInput().runningFold("abcde") { string, operation ->
                operation.applyToString(string)
            }.joinToString("\n")
        }

        testInstructions with runningFold shouldOutput """
            abcde
            ebcda
            edcba
            abcde
            bcdea
            bdeac
            abdec
            ecabd
            decab
        """.trimIndent()
    }

    partTwoTest {
        val runningFoldReverse: Part = {
            parseInput().reversed().runningFold("decab") { string, operation ->
                operation.applyToStringReverse(string)
            }.joinToString("\n")
        }

        testInstructions with runningFoldReverse shouldOutput """
            decab
            ecabd
            abdec
            bdeac
            bcdea
            abcde
            edcba
            ebcda
            abcde
        """.trimIndent()
    }
}