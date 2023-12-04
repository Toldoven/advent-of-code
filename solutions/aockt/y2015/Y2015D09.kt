package aockt.y2015

import aockt.destructible
import io.github.jadarma.aockt.core.Solution

typealias DistanceMap = Map<String, Map<String, Int>>

object Y2015D09 : Solution {

    private fun parseInput(input: String) = input.lineSequence().flatMap {
        val (first, _, second, _, distance) = it.split(' ').destructible
        listOf(
            Triple(first, second, distance.toInt()),
            Triple(second, first, distance.toInt())
        )
    }.groupBy(
        { (from, _, _) -> from },
        { (_, destination, distance) -> destination to distance },
    ).mapValues { (_, value) -> value.toMap() }

    private fun DistanceMap.distanceToCity(from: String, to: String) =
        get(from)?.get(to) ?: throw Exception("Input doesn't contain distances between all cities")

    private fun DistanceMap.possibleRoutes(
        startAt: String,
        visitCities: Set<String>,
        alreadyTraveled: Int = 0,
    ): Sequence<Int> = when (visitCities.size) {
        in 0..1 -> sequenceOf(alreadyTraveled + visitCities.sumOf { distanceToCity(startAt, it) })
        else -> visitCities.asSequence().flatMap { destination ->
            val traveled = alreadyTraveled + distanceToCity(startAt, destination)
            possibleRoutes(destination, visitCities - destination, traveled)
        }
    }

    private fun DistanceMap.possibleRoutes() = keys.flatMap { possibleRoutes(it, keys - it) }

    override fun partOne(input: String) = parseInput(input).possibleRoutes().min()

    override fun partTwo(input: String) = parseInput(input).possibleRoutes().max()
}