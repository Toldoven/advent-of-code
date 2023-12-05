package aockt.y2023

import io.github.jadarma.aockt.core.Solution

typealias AlmanacMap = List<Triple<Long, Long, Long>>

object Y2023D05 : Solution {

    private fun String.parseNumbers() = split(' ').mapNotNull { it.trim().toLongOrNull() }

    private fun parseAlmanacMapList(input: String): List<AlmanacMap> = input.split("\n\n").drop(1).map { map ->
        map.lines().drop(1).map {
            val (destination, source, length) = it.parseNumbers()
            Triple(destination, source, length)
        }
    }

    private fun List<AlmanacMap>.seedLocation(seed: Long) = fold(seed) { acc, ranges ->
        ranges.firstNotNullOfOrNull { (destination, source, length) ->
            val sourceRange = (source..<source + length)
            if (acc in sourceRange) acc + (destination - source) else null
        } ?: acc
    }

    override fun partOne(input: String): Any {
        val (seedsInput, _) = input.split("\n\n")
        val seeds = seedsInput.substringAfter(": ").parseNumbers()
        val almanacMapList = parseAlmanacMapList(input)
        return seeds.minOf { seed -> almanacMapList.seedLocation(seed) }
    }

    override fun partTwo(input: String): Any {
        val (seedInput, _) = input.split("\n\n")

        val seedRanges = seedInput.substringAfter(": ")
            .parseNumbers()
            .chunked(2)
            .map { (start, length) -> (start..<start + length) }

        val almanacMapList = parseAlmanacMapList(input)

        return seedRanges.parallelStream()
            .map { range -> range.minOf { seed -> almanacMapList.seedLocation(seed) } }
            .min(Long::compareTo)
            .get()
    }
}