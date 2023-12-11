package year2015.day23

import framework.InputProvider
import framework.solution

data class ExecutionState(
    val instructions: List<Instruction>,
    val currentInstruction: Int = 0,
    val registers: Map<String, Int> = emptyMap(),
) {
    fun getRegister(register: String) = registers[register] ?: 0

    fun copyRegistersWith(register: String, block: (Int) -> Int): Map<String, Int> {
        val newValue = getRegister(register).run(block)
        return registers + (register to newValue)
    }

    fun asSequence() = generateSequence(this) {
        val instruction = it.instructions.getOrNull(it.currentInstruction) ?: return@generateSequence null
        instruction.applyToState(it)
    }
}

sealed interface Instruction {
    fun applyToState(state: ExecutionState): ExecutionState
}

data class Half(val register: String) : Instruction {
    override fun applyToState(state: ExecutionState) = state.copy(
        registers = state.copyRegistersWith(register) { it / 2 },
        currentInstruction = state.currentInstruction + 1,
    )
}

data class Triple(val register: String) : Instruction {
    override fun applyToState(state: ExecutionState) = state.copy(
        registers = state.copyRegistersWith(register) { it * 3 },
        currentInstruction = state.currentInstruction + 1,
    )
}

data class Increment(val register: String) : Instruction {
    override fun applyToState(state: ExecutionState) = state.copy(
        registers = state.copyRegistersWith(register) { it + 1 },
        currentInstruction = state.currentInstruction + 1,
    )
}

data class Jump(val offset: Int) : Instruction {
    override fun applyToState(state: ExecutionState): ExecutionState = state.copy(
        currentInstruction = state.currentInstruction + offset,
    )
}

data class JumpIfEven(val register: String, val offset: Int) : Instruction {
    override fun applyToState(state: ExecutionState): ExecutionState {
        val offsetBy = if (state.getRegister(register) % 2 == 0) offset else 1
        return state.copy(
            currentInstruction = state.currentInstruction + offsetBy,
        )
    }
}

data class JumpIfOne(val register: String, val offset: Int) : Instruction {
    override fun applyToState(state: ExecutionState): ExecutionState {
        val offsetBy = if (state.getRegister(register) == 1) offset else 1
        return state.copy(
            currentInstruction = state.currentInstruction + offsetBy,
        )
    }
}

fun main() = solution(2015, 23, "Opening the Turing Lock") {

    fun String.parseOffset(): Int {
        val (sign, value) = first() to drop(1).toInt()
        return when (sign) {
            '+' -> value
            '-' -> -value
            else -> throw IllegalArgumentException("$sign is not a sign")
        }
    }

    val inputRegex = """^(\w+) ([+\-]?\w+)?(?:, ([+\-]\w+))?$""".toRegex(RegexOption.MULTILINE)

    fun InputProvider.parseInput() = inputRegex.findAll(input).map {
        val (instruction, first, second) = it.destructured
        when (instruction) {
            "hlf" -> Half(first)
            "tpl" -> Triple(first)
            "inc" -> Increment(first)
            "jmp" -> Jump(first.parseOffset())
            "jie" -> JumpIfEven(first, second.parseOffset())
            "jio" -> JumpIfOne(first, second.parseOffset())
            else -> throw IllegalArgumentException("Unknown instruction: $instruction")
        }
    }.toList()

    partOne {
        val instructions = parseInput()

        ExecutionState(instructions)
            .asSequence()
            .last()
            .getRegister("b")
    }

    partTwo {
        val instructions = parseInput()
        val registers = mapOf("a" to 1)

        ExecutionState(instructions, registers = registers)
            .asSequence()
            .last()
            .getRegister("b")
    }

    partOneTest {
        """
            inc b
            jio b, +2
            tpl b
            inc b
        """.trimIndent() shouldOutput 2
    }
}