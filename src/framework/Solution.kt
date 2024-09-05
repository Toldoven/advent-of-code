package framework

import java.io.File
import java.net.URL

typealias Part = InputProvider.() -> Any?

data class Day(val year: Int, val day: Int) {
    private val paddedDay = day.toString().padStart(2, '0')

    val inputUrl = URL("https://adventofcode.com/$year/day/$day/input")

    private val dayDirectory = File("src/year$year/day$paddedDay/")

    val inputFile = File(dayDirectory, "input.txt")
    fun solutionFile(part: Int) = File(dayDirectory, "part$part.txt")

    fun formatWithTitle(title: String?) = if (title != null) "Day $day: $title" else "Day: $day"
}

fun solution(year: Int, day: Int, title: String? = null, block: Solution.() -> Unit) {
    Solution(Day(year, day)).apply(block).also {
        if (title != null) {
            it.title = title
        }
    }.execute()
}

class Solution(private val day: Day) {

    var title: String? = null

    private var partOne: Part? = null

    private var partTwo: Part? = null

    fun partOne(ignore: Boolean = false, part: Part) {
        if (!ignore) {
            partOne = part
        }
    }

    fun partTwo(ignore: Boolean = false, part: Part) {
        if (!ignore) {
            partTwo = part
        }
    }

    private val partOneTest = PartTestRunner()

    private val partTwoTest = PartTestRunner()

    fun partOneTest(block: PartTestRunner.() -> Unit) {
        partOneTest.apply(block)
    }

    fun partTwoTest(block: PartTestRunner.() -> Unit) {
        partTwoTest.apply(block)
    }

    private fun executePart(partNumber: Int, input: DefaultInputProvider, part: Part?, testRunner: PartTestRunner) {
        if (part == null) {
            return
        }
        testRunner.test(part)
        val result = try {
            part(input)
        } catch (error: Throwable) {
            printRed {
                println("Exception while processing part $partNumber")
            }
            error.printStackTrace()
            return
        }
        val verifiedSolution = day.solutionFile(partNumber)
            .takeIf { it.isFile }
            ?.readText()
            ?.trim()

        if (verifiedSolution != null) {
            if (result.toString() == verifiedSolution) {
                printGreen {
                    println("Verified solution: $result")
                }
            } else {
                printRed {
                    println("Wrong solution")
                    println(" ⇒ Got: $result")
                    println(" ⇒ Expected: $verifiedSolution")
                }
            }
            return
        }
        printYellow {
            println("Unverified solution: $result")
        }
    }

    fun execute() {
        println(prettyFrame(day.formatWithTitle(title)))
        val inputFetcher = InputFetcher(day)
        val input = inputFetcher.fetchInput()
        println(prettyLine('─', "Part One"))
        executePart(1, input, partOne, partOneTest)
        println(prettyLine('─', "Part Two"))
        executePart(2, input, partTwo, partTwoTest)
    }
}