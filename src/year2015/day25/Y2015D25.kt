package year2015.day25

import framework.InputProvider
import framework.solution
import utils.IntVec2



fun main() = solution(2015, 25, "Let It Snow") {

    val regex =
        Regex("""To continue, please consult the code grid in the manual.\s*Enter the code at row (\d+), column (\d+).""")

    fun InputProvider.parseInput() = regex.matchEntire(input)?.let {
        IntVec2(it.groupValues[1].toInt(), it.groupValues[2].toInt())
    } ?: throw IllegalArgumentException("Invalid input")


    partOne {
        val (x, y) = parseInput()
        val flatIndex = (x + y - 2) * (x + y - 1) / 2 + y - 1
        generateSequence(20151125L) { it * 252533L % 33554393L }.elementAt(flatIndex)
    } 

}