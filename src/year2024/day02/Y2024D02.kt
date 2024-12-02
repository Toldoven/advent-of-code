package year2024.day02

import framework.InputProvider
import framework.solution
import kotlin.math.abs
import kotlin.math.sign

fun main() = solution(2024, 2, "Red-Nosed Reports") {

    fun InputProvider.parseInput() = lines.map { line ->
        line.split(' ').map {
            it.toInt()
        }
    }

    fun List<Int>.isValid() = zipWithNext()
        .map { (a, b) -> b - a }
        .let { list ->
            val isDescendingOrAscending = list.all { it > 0 } || list.all { it < 0 }
            val isDiffWithinRange = list.all { diff -> abs(diff) in 1..3 }
            isDescendingOrAscending && isDiffWithinRange
        }

    partOne {
        parseInput().count { it.isValid() }
    } 
    
    partTwo {
        parseInput().count { list ->
            list.indices.map { skipIndex ->
                list.filterIndexed { index, _ -> index != skipIndex }
            }.any {
                it.isValid()
            }
        }
    }
}