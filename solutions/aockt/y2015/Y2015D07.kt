package aockt.y2015

import aockt.splitFromEnd
import io.github.jadarma.aockt.core.Solution

typealias Circuit = MutableMap<String, UShort>

sealed class Signal {
    fun fromCircuit(circuit: Circuit) = when (this) {
        is Value -> value
        is Wire -> circuit[wire]
    }
}

data class Value(val value: UShort) : Signal()

data class Wire(val wire: String) : Signal()

fun Circuit.getSignal(signal: Signal) = signal.fromCircuit(this)

fun String.toSignal() = if (all { it.isDigit() }) {
    Value(toUShort())
} else {
    Wire(this)
}

sealed class Operation {
    // Returns false if you can't make the connection
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

data class And(val a: Signal, val b: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        val first = circuit.getSignal(a) ?: return false
        val second = circuit.getSignal(b) ?: return false
        circuit[output] = first and second
        return true
    }
}

data class Or(val a: Signal, val b: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        val first = circuit.getSignal(a) ?: return false
        val second = circuit.getSignal(b) ?: return false
        circuit[output] = first or second
        return true
    }
}

data class RShift(val a: Signal, val b: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        val first = circuit.getSignal(a) ?: return false
        val second = circuit.getSignal(b) ?: return false
        circuit[output] = first.toInt().shr(second.toInt()).toUShort()
        return true
    }
}

data class LShift(val a: Signal, val b: Signal) : Operation() {
    override fun connectOutput(circuit: Circuit, output: String): Boolean {
        val first = circuit.getSignal(a) ?: return false
        val second = circuit.getSignal(b) ?: return false
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
        val remainingInstruction = instructions.filterNot { (operation, output) ->
            operation.connectOutput(circuit, output)
        }
        return if (remainingInstruction.isEmpty()) {
            circuit
        } else {
            processInstructions(remainingInstruction, circuit)
        }
    }

    override fun partOne(input: String) = processInstructions(parseInput(input))
        .getOrDefault("a", 0u)

    override fun partTwo(input: String): UShort {
        val instructions = parseInput(input)
        val a = processInstructions(instructions).getOrDefault("a", 0u)
        val newInstructions = instructions.map {
            if (it.second == "b") {
                Connect(Value(a)) to it.second
            } else {
                it
            }
        }
        return processInstructions(newInstructions)
            .getOrDefault("a", 0u)
    }
}