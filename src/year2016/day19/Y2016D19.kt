package year2016.day19

import framework.InputProvider
import framework.solution
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.pow

fun main() = solution(2016, 19, "An Elephant Named Joseph") {

    fun InputProvider.parseInput() = input.toInt()

    partOne {
        val target = parseInput()
        // Of course, I didn't figure it out myself:
        // https://www.reddit.com/r/adventofcode/comments/5j4lp1/comment/dbdf4up/
        val l = 2.0.pow(floor(log2(target.toDouble()))).toInt()
        (target - l) * 2 + 1
    }

    partTwo {
        val target = parseInput()
        // Of course, I didn't figure it out myself:
        // https://www.reddit.com/r/adventofcode/comments/5j4lp1/comment/dbdf50n/
        generateSequence(1) { it * 3 }
            .first { it * 3 > target }
            .let { target - it }
    }
}