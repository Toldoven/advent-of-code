package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D01 : Solution {

    private fun Iterable<String>.sumOfFirstAndLastDigit() = sumOf { line ->
        val firstDigit = line.find { it.isDigit() }?.digitToInt() ?: 0
        val lastDigit = line.findLast { it.isDigit() }?.digitToInt() ?: 0
        firstDigit * 10 + lastDigit
    }

    override fun partOne(input: String) = input.lines().sumOfFirstAndLastDigit()

    private val numberMap = listOf(
        "zero", "one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine",
    ).mapIndexed { index, key -> key to "$key$index$key"}

    override fun partTwo(input: String) = input.lines().map {
        numberMap.fold(it) { line, (pattern, replaceWith) ->
            line.replace(pattern, replaceWith)
        }
    }.sumOfFirstAndLastDigit()

//    // Initial Solution
//    override fun partTwo(input: String) = input.lines().sumOf { line ->
//        val numbers = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
//        val indexOfFirstString = line.indexOfAny(numbers)
//        val indexOfLastString = line.lastIndexOfAny(numbers)
//        val indexOfFirstDigit = line.indexOfFirst { it.isDigit() }
//        val indexOfLastDigit = line.indexOfLast { it.isDigit() }
//        val first = if (indexOfFirstString in 0..indexOfFirstDigit) {
//            numbers.indexOfFirst { line.drop(indexOfFirstString).startsWith(it) }
//        } else {
//            line[indexOfFirstDigit].digitToInt()
//        }
//        val last = if (indexOfLastDigit in 0..indexOfLastString) {
//            numbers.indexOfLast { line.drop(indexOfLastString).startsWith(it) }
//        } else {
//            line[indexOfLastDigit].digitToInt()
//        }
//        first * 10 + last
//    }
}