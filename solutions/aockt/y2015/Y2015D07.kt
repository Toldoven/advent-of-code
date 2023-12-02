package aockt.y2015

import aockt.isInt
import aockt.splitFromEnd
import io.github.jadarma.aockt.core.Solution

typealias Circuit = MutableMap<String, UShort>

sealed class Signal

data class Value(val value: UShort) : Signal()

data class Wire(val wire: String) : Signal()

fun Circuit.getSignal(signal: Signal) = when (signal) {
    is Value -> signal.value
    is Wire -> this[signal.wire]
}

fun String.toSignal() = if (isInt()) Value(toUShort()) else Wire(this)

sealed class Operation {
    // Return false if you can't make the connection
    abstract fun connectOutput(circuit: Circuit, output: String): Boolean
}

data class Connect(val signal: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        circuit[output] = circuit.getSignal(signal) ?: return false
        return true
    }
}

data class Not(val signal: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        circuit[output] = circuit.getSignal(signal)?.inv() ?: return false
        return true
    }
}

data class And(val first: Signal, val second: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        val first = circuit.getSignal(first) ?: return false
        val second = circuit.getSignal(second) ?: return false
        circuit[output] = first and second
        return true
    }
}

data class Or(val first: Signal, val second: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        val first = circuit.getSignal(first) ?: return false
        val second = circuit.getSignal(second) ?: return false
        circuit[output] = first or second
        return true
    }
}

data class RShift(val first: Signal, val second: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        val first = circuit.getSignal(first) ?: return false
        val second = circuit.getSignal(second) ?: return false
        circuit[output] = first.toInt().shr(second.toInt()).toUShort()
        return true
    }
}

data class LShift(val first: Signal, val second: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        val first = circuit.getSignal(first) ?: return false
        val second = circuit.getSignal(second) ?: return false
        circuit[output] = first.toInt().shl(second.toInt()).toUShort()
        return true
    }
}

object Y2015D07 : Solution {

    private fun parseInput(input: String) = input.lineSequence().map { line ->
        val split = line.splitFromEnd(' ', limit = 3)
        val operation = split[0].split(' ')
        val output = split[2]
        with(operation) {
            when (size) {
                1 -> Connect(get(0).toSignal())
                2 -> Not(get(1).toSignal())
                3 -> when (get(1)) {
                    "AND" -> And(get(0).toSignal(), get(2).toSignal())
                    "OR" -> Or(get(0).toSignal(), get(2).toSignal())
                    "RSHIFT" -> RShift(get(0).toSignal(), get(2).toSignal())
                    "LSHIFT" -> LShift(get(0).toSignal(), get(2).toSignal())
                    else -> throw IllegalArgumentException()
                }

                else -> throw IllegalArgumentException()
            }
        } to output
    }.toList()

    private fun processInstructions(
        instructions: List<Pair<Operation, String>>,
        circuit: Circuit = mutableMapOf(),
    ): Circuit {
        val remainingInstructions = instructions.filterNot { (operation, output) ->
            operation.connectOutput(circuit, output)
        }
        return if (remainingInstructions.isNotEmpty()) {
            processInstructions(remainingInstructions, circuit)
        } else {
            circuit
        }
    }

    private fun processInstructionsGetA(
        instructions: List<Pair<Operation, String>>,
    ) = processInstructions(instructions).getOrDefault("a", 0u)

    override fun partOne(input: String) = parseInput(input).let(::processInstructionsGetA)

    override fun partTwo(input: String): UShort {
        val instructions = parseInput(input)
        val a = processInstructionsGetA(instructions)
        val newInstructions = instructions.map { (operation, output) ->
            (if (output == "b") Connect(Value(a)) else operation) to output
        }
        return processInstructionsGetA(newInstructions)
    }
}