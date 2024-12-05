package year2024.day05

import framework.InputProvider
import framework.solution
import utils.map
import utils.splitOnce

typealias Rule = Pair<Int, Int>

typealias Update = List<Int>

fun main() = solution(2024, 5, "Print Queue") {

    fun InputProvider.parseInput(): Pair<List<Rule>, List<Update>> {
        val (rulesString, updatesString) = input.splitOnce("\n\n")

        val rules = rulesString.lines().map {
            it.splitOnce('|').map { it.toInt() }
        }

        val updates = updatesString.lines().map {
            it.split(',').map { it.toInt() }
        }

        return rules to updates
    }

    fun Rule.isUpdateValid(update: Update): Boolean {
        val (a, b) = this.map { update.indexOf(it) }
        return if (a != -1 && b != -1) a < b else true
    }

    partOne {
        val (rules, updates) = parseInput()

        updates.filter { update ->
            rules.all { rule ->
                rule.isUpdateValid(update)
            }
        }
            .sumOf {
                it[it.size / 2]
            }
    }

    partTwo {
        val (rules, updates) = parseInput()

        updates.filterNot { update ->
            rules.all { rule ->
                rule.isUpdateValid(update)
            }
        }
            .map {
                it.sortedWith { a, b ->
                    when {
                        rules.contains(a to b) -> 1
                        rules.contains(b to a) -> -1
                        else -> 0
                    }
                }
            }
            .sumOf {
                it[it.size / 2]
            }
    }

    val testInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()
    
    partOneTest {
        testInput shouldOutput 143
    }

    partTwoTest {
        testInput shouldOutput 123
    }
}