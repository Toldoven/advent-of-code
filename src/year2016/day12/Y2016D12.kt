package year2016.day12

import framework.InputProvider
import framework.solution
import utils.isInt

enum class Register {
    A, B, C, D;

    companion object {
        fun fromString(string: String) = when(string) {
            "a" -> A
            "b" -> B
            "c" -> C
            "d" -> D
            else -> throw IllegalArgumentException("Unknown register: $string")
        }

        fun emptyMap() = Register.entries.associateWith { 0 }
    }
}


sealed class Value {
    data class Lit(val value: Int): Value()
    data class Reg(val register: Register): Value()

    companion object {
        fun fromString(string: String) =
            if (string.isInt()) {
                Lit(string.toInt())
            } else {
                Reg(Register.fromString(string))
            }
    }
}


data class State(
    val instructionList: List<Instruction>,
    val registerMap: Map<Register, Int> = Register.emptyMap(),
    val instructionPointer: Int = 0,
) {
    init {
        assert(instructionPointer >= 0) { "Pointer should never go below 0" }
    }

    fun getValue(value: Value): Int = when(value) {
        is Value.Lit -> value.value
        is Value.Reg -> registerMap[value.register] ?: 0
    }

    private val currentInstruction get() = instructionList.getOrNull(instructionPointer)

    fun asSequence() = generateSequence(this) { state ->
        state.currentInstruction?.applyToState(state)
    }
}


sealed class Instruction {
    // cpy x y: copies x (either an integer or the value of a register) into register y.
    data class Cpy(val value: Value, val register: Register): Instruction() {
        override fun applyToState(state: State): State {
            val getValue = state.getValue(value)
            val newEntry = register to getValue
            return state.copy(
                registerMap = state.registerMap + newEntry,
                instructionPointer = state.instructionPointer + 1
            )
        }
    }

    // inc x: increases the value of register x by one.
    data class Inc(val register: Register): Instruction() {
        override fun applyToState(state: State): State {
            val currentValue = state.registerMap[register] ?: 0
            val newEntry = register to currentValue + 1
            return state.copy(
                registerMap = state.registerMap + newEntry,
                instructionPointer = state.instructionPointer + 1
            )
        }
    }

    // dec x: decreases the value of register x by one.
    data class Dec(val register: Register): Instruction() {
        override fun applyToState(state: State): State {
            val currentValue = state.registerMap[register] ?: 0
            val newEntry = register to currentValue - 1
            return state.copy(
                registerMap = state.registerMap + newEntry,
                instructionPointer = state.instructionPointer + 1,
            )
        }
    }

    // jnz x y: jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
    data class Jnz(val value: Value, val amount: Int): Instruction() {
        override fun applyToState(state: State): State {
            val getValue = state.getValue(value)

            return if (getValue != 0) {
                state.copy(instructionPointer = state.instructionPointer + amount)
            } else {
                state.copy(instructionPointer = state.instructionPointer + 1)
            }
        }
    }


    abstract fun applyToState(state: State): State

    companion object {
        fun fromString(string: String) = string.split(' ').let { split ->
            when (val instructionString = split[0]) {
                "cpy" -> Cpy(Value.fromString(split[1]), Register.fromString(split[2]))
                "inc" -> Inc(Register.fromString(split[1]))
                "dec" -> Dec(Register.fromString(split[1]))
                "jnz" -> Jnz(Value.fromString(split[1]), split[2].toInt())
                else -> throw IllegalArgumentException("Unknown instruction: $instructionString")
            }
        }
    }
}




fun main() = solution(2016, 12, "Leonardo's Monorail") {

    fun InputProvider.parseInstructions() = lines.map { Instruction.fromString(it) }

    partOne {
        State(parseInstructions())
            .asSequence()
            .last()
            .registerMap[Register.A]
    }
    
    partOneTest {
        """
            cpy 41 a
            inc a
            inc a
            dec a
            jnz a 2
            dec a
        """.trimIndent() shouldOutput 42
    }

    partTwo {
        State(
            parseInstructions(),
            registerMap = Register.emptyMap() + (Register.C to 1)
        )
            .asSequence()
            .last()
            .registerMap[Register.A]
    }
}