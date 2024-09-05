package year2016.day08

import framework.InputProvider
import framework.solution
import utils.IntVec2
import utils.letSecond
import utils.map
import utils.splitOnce

enum class Axis { COLUMN, ROW }

class Display {
    private val size = IntVec2(50, 6)
    private val list = MutableList(size.x * size.y) { false }

    fun applyCommand(command: Command) {
        when (command) {
            is Command.Rect -> applyRect(command)
            is Command.Rotate -> applyRotate(command)
        }
    }

    private fun displayIndexToListIndex(index: IntVec2): Int {
        if (index.x >= size.x || index.y >= size.y) {
            throw IndexOutOfBoundsException()
        }
        return index.x + size.x * index.y
    }

    private fun applyRect(command: Command.Rect) {
        (0..<command.size.x).forEach { x ->
            (0..<command.size.y).forEach { y ->
                val index = displayIndexToListIndex(IntVec2(x, y))
                list[index] = true
            }
        }
    }

    private fun applyRotate(command: Command.Rotate) {
        when (command.axis) {
            Axis.COLUMN -> shiftColumn(command.index, command.amount)
            Axis.ROW -> shiftRow(command.index, command.amount)
        }
    }

    private fun shiftRow(index: Int, amount: Int) {
        val amountCapped = amount % size.x
        (0..<size.x)
            .map { x -> displayIndexToListIndex(IntVec2(x, index)) }
            .map { listIndex -> list[listIndex] }
            .let {
                it.takeLast(amountCapped) + it.dropLast(amountCapped)
            }
            .forEachIndexed { x, value ->
                val listIndex = displayIndexToListIndex(IntVec2(x, index))
                list[listIndex] = value
            }
    }

    private fun shiftColumn(index: Int, amount: Int) {
        val amountCapped = amount % size.y
        (0..<size.y)
            .map { y -> displayIndexToListIndex(IntVec2(index, y)) }
            .map { listIndex -> list[listIndex] }
            .let {
                it.takeLast(amountCapped) + it.dropLast(amountCapped)
            }
            .forEachIndexed { y, value ->
                val listIndex = displayIndexToListIndex(IntVec2(index, y))
                list[listIndex] = value
            }
    }

    fun countPixels(): Int = list.count { it }

    fun asString() = (0..<size.y).joinToString("\n") { y ->
        (0..<size.x).joinToString("") { x ->
            val value = list[displayIndexToListIndex(IntVec2(x, y))]
            if (value) "#" else "."
        }
    }
}

sealed class Command {
    class Rect(val size: IntVec2): Command()
    class Rotate(val axis: Axis, val index: Int, val amount: Int): Command()
}

fun main() = solution(2016, 8, "Two-Factor Authentication") {

    fun InputProvider.parseInput() = lineSequence.map { line ->
        val split = line.split(' ')

        when (split[0]) {
            "rect" -> {
                require(split.size == 2) { "Wrong command format"}
                val (width, height) = split[1].splitOnce('x').map { it.toInt() }
                Command.Rect(IntVec2(width, height))
            }
            "rotate" -> {
                require(split.size == 5) { "Wrong command format"}

                val axis = when(split[1]) {
                    "row" -> Axis.ROW
                    "column" -> Axis.COLUMN
                    else -> throw IllegalArgumentException("Unknown axis")
                }

                val (axisCheck, index) = split[2].splitOnce('=').letSecond { it.toInt() }

                when (axis) {
                    Axis.COLUMN -> assert(axisCheck == "x")
                    Axis.ROW -> assert(axisCheck == "y")
                }

                assert(split[3] == "by")

                val amount = split[4].toInt()

                Command.Rotate(axis, index, amount)
            }
            else -> throw IllegalArgumentException("Unknown command")
        }
    }

    partOne {
        val display = Display()
        parseInput().forEach {
            display.applyCommand(it)
        }
        display.countPixels()
    }
    
    partOneTest {
        """
            rect 3x2
            rotate column x=1 by 1
            rotate row y=0 by 4
            rotate column x=1 by 1
        """.trimIndent() shouldOutput 6
    }

    partTwo {
        val display = Display()
        parseInput().forEach {
            display.applyCommand(it)
        }
        display.asString()
    }
}