package year2024.day07

import arrow.core.MemoizedDeepRecursiveFunction
import framework.InputProvider
import framework.solution
import utils.parseNumbersLong
import utils.splitOnce
import java.util.LinkedList

enum class Operation(val calculate: (Long, Long) -> Long) {
    ADD(Long::plus),
    MULTIPLY(Long::times),
    CONCAT({ a, b -> "$a$b".toLong() });
}

fun main() = solution(2024, 7, "Bridge Repair") {

    fun InputProvider.parseInput() = lines.map {
        val (first, list) = it.splitOnce(": ")
        first.toLong() to list.parseNumbersLong(' ')
    }

    fun possibleOperations(operationList: List<Operation>) =
        MemoizedDeepRecursiveFunction<Int, List<List<Operation>>> { size ->
            when {
                size == 0 -> listOf(emptyList())
                else -> operationList.flatMap { op ->
                    callRecursive(size - 1).map { it + op }
                }
            }
        }

    fun InputProvider.solve(operationList: List<Operation>) = possibleOperations(operationList)
        .let { possibleOperations ->
            parseInput().filter { (target, list) ->
                possibleOperations(list.size - 1).any { it ->
                    val operations = LinkedList(it)
                    val result = list.reduce { a, b ->
                        operations.removeFirst().calculate(a, b)
                    }
                    result == target
                }
            }.sumOf {
                it.first
            }
        }

    partOne {
        solve(
            listOf(
                Operation.ADD,
                Operation.MULTIPLY,
            )
        )
    }

    partTwo {
        solve(Operation.entries)
    }


    val testInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 3749
    }

    partTwoTest {
        testInput shouldOutput 11387
    }
}