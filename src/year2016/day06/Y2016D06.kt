package year2016.day06

import framework.InputProvider
import framework.solution
import utils.swapRowsAndColumns

fun main() = solution(2016, 6, "Signals and Noise") {

    fun InputProvider.solve(part2: Boolean = false) = lines
        .map { it.toList() }
        .swapRowsAndColumns()
        .map { column ->
            val occurrences = column
                .groupingBy { it }
                .eachCount()

            val entry = if (part2) {
                occurrences.minBy { it.value }
            } else {
                occurrences.maxBy { it.value }
            }

            entry.key
        }.joinToString("")

    partOne {
        solve()
    }

    val testInput = """
        eedadn
        drvtee
        eandsr
        raavrd
        atevrs
        tsrnev
        sdttsa
        rasrtv
        nssdts
        ntnada
        svetve
        tesnvt
        vntsnd
        vrdear
        dvrsen
        enarar
    """.trimIndent()

    partOneTest {
        testInput shouldOutput "easter"
    }

    partTwo {
        solve(true)
    }

    partTwoTest {
        testInput shouldOutput "advent"
    }
}