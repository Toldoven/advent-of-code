package year2015.day13

import framework.InputProvider
import framework.solution

typealias HapinessMap = Map<String, Map<String, Int>>

fun main() = solution(2015, 13, "Knights of the Dinner Table") {

    fun InputProvider.parseInput(): HapinessMap = lines.map { line ->
        val split = line.dropLast(1).split(' ')
        val score = split[3].toInt().let { if (split[2] == "lose") -it else it }
        Triple(split[0], split[10], score)
    }.groupBy(
        { (name, _, _) -> name },
        { (_, nextTo, score) -> nextTo to score },
    ).mapValues { it.value.toMap() }

    fun HapinessMap.happinessFor(personOne: String, personTwo: String): Int {
        return (get(personOne)?.get(personTwo) ?: 0) + (get(personTwo)?.get(personOne) ?: 0)
    }
    
    fun HapinessMap.bestOutcome(
        addSelf: Boolean,
        current: List<String> = emptyList(),
        remaining: Set<String> = keys,
        happiness: Int = 0,
    ): Int = if (remaining.isEmpty()) {
        happiness + if (addSelf) {
            0
        } else {
            happinessFor(current.first(), current.last())
        }
    } else {
        remaining.maxOf { nextPerson ->
            val change = current.lastOrNull()
                ?.let { lastPerson -> happinessFor(nextPerson, lastPerson) }
                ?: 0
            bestOutcome(
                addSelf,
                current + nextPerson,
                remaining - nextPerson,
                happiness + change
            )
        }
    }

    partOne {
        parseInput().bestOutcome(false)
    }

    partTwo {
        parseInput().bestOutcome(true)
    }

    val testInput = """
        Alice would gain 54 happiness units by sitting next to Bob.
        Alice would lose 79 happiness units by sitting next to Carol.
        Alice would lose 2 happiness units by sitting next to David.
        Bob would gain 83 happiness units by sitting next to Alice.
        Bob would lose 7 happiness units by sitting next to Carol.
        Bob would lose 63 happiness units by sitting next to David.
        Carol would lose 62 happiness units by sitting next to Alice.
        Carol would gain 60 happiness units by sitting next to Bob.
        Carol would gain 55 happiness units by sitting next to David.
        David would gain 46 happiness units by sitting next to Alice.
        David would lose 7 happiness units by sitting next to Bob.
        David would gain 41 happiness units by sitting next to Carol.
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 330
    }

    partTwoTest {
        testInput shouldOutput 286
    }
}