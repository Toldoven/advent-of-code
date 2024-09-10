package year2016.day23
import framework.Part
import framework.solution
import utils.replaceAt
import utils.splitOnce

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
        fun fromString(string: String): Value {
            val asInt = string.toIntOrNull()
            return if (asInt != null) {
                Lit(asInt)
            } else {
                Reg(Register.fromString(string))
            }
        }
    }
}


data class State(
    val instructionStringList: List<String>,
    val registerMap: Map<Register, Int> = Register.emptyMap(),
    val instructionPointer: Int = 0,
) {
    init {
        assert(instructionPointer >= 0) { "Pointer should never go below 0" }
    }

    fun setRegister(register: Register, value: Int) = copy(
        registerMap = registerMap + (register to value)
    )

    fun getValue(value: Value): Int = when(value) {
        is Value.Lit -> value.value
        is Value.Reg -> registerMap[value.register] ?: 0
    }

    fun asSequence() = generateSequence(this) { state ->

        val instructionString = state.instructionStringList.getOrNull(state.instructionPointer)
            ?: return@generateSequence null

        val result = try {
            val instruction = Instruction.fromString(instructionString)
            instruction.applyToState(state)
        } catch(error: Throwable) {
            error.printStackTrace()
            state.copy(instructionPointer = state.instructionPointer + 1)
        }

        return@generateSequence result
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
    data class Jnz(val value: Value, val amount: Value): Instruction() {
        override fun applyToState(state: State): State {
            val getValue = state.getValue(value)

            return if (getValue != 0) {
                state.copy(instructionPointer = state.instructionPointer + state.getValue(amount))
            } else {
                state.copy(instructionPointer = state.instructionPointer + 1)
            }
        }
    }

    data class Tgl(val shift: Value): Instruction() {
        override fun applyToState(state: State): State {

            val index = state.instructionPointer + state.getValue(shift)

            val currentInstruction = state.instructionStringList.getOrNull(index)
                ?: return state.copy(instructionPointer = state.instructionPointer + 1)

            val (instructionName, arguments) = currentInstruction.splitOnce(' ')

            val argumentCount = arguments.split(' ').size

            val newInstructionName = when (argumentCount) {
                1 -> when (instructionName) {
                    "inc" -> "dec"
                    else -> "inc"
                }
                2 -> when (instructionName) {
                    "jnz" -> "cpy"
                    else -> "jnz"
                }
                else -> throw IllegalArgumentException("There should be no instruction with more than 2 arguments")
            }

            val newList = state.instructionStringList.replaceAt(index, "$newInstructionName $arguments")

            return state.copy(
                instructionStringList = newList,
                instructionPointer = state.instructionPointer + 1,
            )
        }
    }


    abstract fun applyToState(state: State): State

    companion object {
        fun fromString(string: String) = string.split(' ').let { split ->
            when (val instructionString = split[0]) {
                "cpy" -> Cpy(Value.fromString(split[1]), Register.fromString(split[2]))
                "inc" -> Inc(Register.fromString(split[1]))
                "dec" -> Dec(Register.fromString(split[1]))
                "jnz" -> Jnz(Value.fromString(split[1]), Value.fromString((split[2])))
                "tgl" -> Tgl(Value.fromString(split[1]))
                else -> throw IllegalArgumentException("Unknown instruction: $instructionString")
            }
        }
    }
}




fun main() = solution(2016, 23, "Safe Cracking") {

    partOne {
        State(lines)
            .setRegister(Register.A, 7)
            .asSequence()
            .last()
            .registerMap[Register.A]
    }

    // TODO: This is a brute force solution and there is a better one
    partTwo {
        State(lines)
            .setRegister(Register.A, 12)
            .asSequence()
            .last()
            .registerMap[Register.A]
    }

    partOneTest {

        val testPart: Part = {
            State(lines)
                .asSequence()
                .last()
                .registerMap[Register.A]
        }

        """
            cpy 2 a
            tgl a
            tgl a
            tgl a
            cpy 1 a
            dec a
            dec a
        """.trimIndent() with testPart shouldOutput 3
    }

}