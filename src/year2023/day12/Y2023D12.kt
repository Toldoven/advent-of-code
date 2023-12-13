package year2023.day12

import framework.InputProvider
import framework.solution
import utils.parseNumbersInt
import utils.repeat

enum class Status(val char: Char) {
    OPERATIONAL('.'), DAMAGED('#'), UNKNOWN('?');

    override fun toString(): String = this.char.toString()

    companion object {
        private val charMap = Status.entries.associateBy { it.char }

        fun fromChar(char: Char) =
            charMap[char] ?: throw IllegalArgumentException("This character is not a status: $char")
    }
}

typealias Record = Pair<List<Status>, List<Int>>

fun main() = solution(2023, 12) {

    fun InputProvider.parseInput(repeat: Int = 1): List<Record> = lines.map { line ->
        val (first, second) = line.split(" ")
        val statusList = listOf(first).repeat(repeat).joinToString("?").map { Status.fromChar(it) }
        val numberList = listOf(second).repeat(repeat).joinToString(",").parseNumbersInt(',')
        statusList to numberList
    }

    // Basically copied from https://github.com/Jadarma/advent-of-code-kotlin-solutions/blob/main/solutions/aockt/y2023/Y2023D12.kt
    // I absolutely hate this problem and how many edge cases there are. Fuck this shit
    // It also doesn't work with MemoizedDeepRecursiveFunction for some reason. TODO: Figure out why?
    fun countSolutions(record: Record, memo: MutableMap<Record, Long> = mutableMapOf()): Long = memo.getOrPut(record) {
        val (springStatusList, damagedList) = record

        if (springStatusList.isEmpty()) {
            return@getOrPut if (damagedList.isEmpty()) 1 else 0
        }

        if (damagedList.isEmpty()) {
            return@getOrPut if (Status.DAMAGED !in springStatusList) 1 else 0
        }

        val (springStatus, nextDamaged) = springStatusList.first() to damagedList.first()

        val countSolutionsWithNextOperational = if (springStatus != Status.DAMAGED) {
            countSolutions(springStatusList.drop(1) to damagedList, memo)
        } else 0

        val countSolutionsWithNextDamaged = when {
            springStatus == Status.OPERATIONAL -> 0
            nextDamaged > springStatusList.size -> 0
            springStatusList.take(nextDamaged).any { it == Status.OPERATIONAL } -> 0
            springStatusList.size != nextDamaged && springStatusList[nextDamaged] == Status.DAMAGED -> 0
            else -> countSolutions(springStatusList.drop(nextDamaged + 1) to damagedList.drop(1), memo)
        }

        countSolutionsWithNextOperational + countSolutionsWithNextDamaged
    }

    partOne {
        parseInput().sumOf { countSolutions(it) }
    }

    partTwo {
        parseInput(5).sumOf { countSolutions(it) }
    }

    val testInput = """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 21
    }

    partTwoTest {
        testInput shouldOutput 525152
    }
}