package year2015.day09

import framework.InputProvider
import framework.solution

typealias DistanceMap = Map<String, Map<String, Int>>

fun main() = solution(2015, 9, "All in a Single Night") {

    fun InputProvider.parseInput() = lineSequence.flatMap {
        val (first, _, second, _, distance) = it.split(' ')
        listOf(
            Triple(first, second, distance.toInt()),
            Triple(second, first, distance.toInt())
        )
    }.groupBy(
        { (from, _, _) -> from },
        { (_, destination, distance) -> destination to distance },
    ).mapValues { it.value.toMap() }

    fun DistanceMap.distanceToCity(from: String, to: String) =
        get(from)?.get(to) ?: throw Exception("Input doesn't contain distances between all cities")

    fun DistanceMap.possibleRoutes(
        startAt: String,
        visitCities: Set<String>,
        alreadyTraveled: Int = 0,
    ): Sequence<Int> = when (visitCities.size) {
        in 0..1 -> visitCities.asSequence().map { distanceToCity(startAt, it) + alreadyTraveled }
        else -> visitCities.asSequence().flatMap { destination ->
            val traveled = alreadyTraveled + distanceToCity(startAt, destination)
            possibleRoutes(destination, visitCities - destination, traveled)
        }
    }

    fun DistanceMap.possibleRoutes() = keys.flatMap { possibleRoutes(it, keys - it) }

    partOne {
        parseInput().possibleRoutes().min()
    }

    partTwo {
        parseInput().possibleRoutes().max()
    }

    val testInput = """
        London to Dublin = 464
        London to Belfast = 518
        Dublin to Belfast = 141
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 605
    }

    partTwoTest {
        testInput shouldOutput 982
    }
}