package year2016.day03

import framework.InputProvider
import framework.solution
import utils.swapRowsAndColumns


typealias Triangle = List<Int>


fun main() = solution(2016, 3, "Squares With Three Sides") {

    val inputRegex = Regex("\\S+")

    fun InputProvider.parseInput() = lines.map { line ->
        inputRegex.findAll(line).map { it.value.toInt() }.toList()
    }

    fun List<Triangle>.countValidTriangles() = count {
        val max = it.withIndex().maxBy { (_, value) -> value }
        it.filterIndexed { index, _ -> index != max.index }.sum() > max.value
    }

    partOne {
        parseInput().countValidTriangles()
    } 
    
    partTwo {
        parseInput().chunked(3)
            .flatMap { chunk -> chunk.swapRowsAndColumns() }
            .countValidTriangles()
    }
}