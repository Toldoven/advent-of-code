package year2023.day05

import framework.solution

typealias AlmanacMap = List<Triple<Long, Long, Long>>

fun main() = solution(2023, 5, "If You Give A Seed A Fertilizer") {

    fun String.parseNumbers() = split(' ').mapNotNull { it.toLongOrNull() }

    fun parseAlmanacMapList(input: String): List<AlmanacMap> = input.split("\n\n").drop(1).map { map ->
        map.lines().drop(1).map {
            val (destination, source, length) = it.parseNumbers()
            Triple(destination, source, length)
        }
    }

    fun List<AlmanacMap>.seedLocation(seed: Long) = fold(seed) { acc, ranges ->
        ranges.firstNotNullOfOrNull { (destination, source, length) ->
            val sourceRange = (source..<source + length)
            if (acc in sourceRange) acc + (destination - source) else null
        } ?: acc
    }

    partOne {
        val (seedsInput, _) = input.split("\n\n")
        val seeds = seedsInput.substringAfter(": ").parseNumbers()
        val almanacMapList = parseAlmanacMapList(input)
        seeds.minOf { seed -> almanacMapList.seedLocation(seed) }
    }

    partTwo {
        val (seedInput, _) = input.split("\n\n")

        val seedRanges = seedInput.substringAfter(": ")
            .parseNumbers()
            .chunked(2)
            .map { (start, length) -> (start..<start + length) }

        val almanacMapList = parseAlmanacMapList(input)

        seedRanges.parallelStream()
            .map { range -> range.minOf { seed -> almanacMapList.seedLocation(seed) } }
            .min(Long::compareTo)
            .get()
    }

    val testInput = """
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 35
    }

    partTwoTest {
        testInput shouldOutput 46
    }
}