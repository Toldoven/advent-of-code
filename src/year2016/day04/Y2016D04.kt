package year2016.day04

import framework.InputProvider
import framework.solution


data class Room(
    val encryptedNameList: List<String>,
    val sectorId: Int,
    val checksum: String,
) {
    private val encryptedName: String = encryptedNameList.joinToString("")

    fun isReal(): Boolean {

        val result = encryptedName
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedWith(compareByDescending<Pair<Char, Int>> { it.second }.thenBy { it.first })
            .map { it.first }
            .joinToString("")

        return result.startsWith(checksum)
    }

    private fun Char.decrypt(): Char {
        val alphabetIndex = this - 'a'
        val decodedAlphabetIndex = (alphabetIndex + sectorId) % 26
        return 'a' + decodedAlphabetIndex
    }

    fun decrypt() = encryptedNameList.joinToString(" ") { word ->
        word.map { it.decrypt() }.joinToString("")
    }

    companion object {
        fun fromString(string: String): Room {
            val split = string.split('-')
            val (sectorIdString, checksum) = split.last().split('[', ']')
            val encryptedNameList = split.dropLast(1)
            return Room(encryptedNameList, sectorIdString.toInt(), checksum)
        }
    }
}

fun main() = solution(2016, 4, "Security Through Obscurity") {

    fun InputProvider.parseInput() = lines.map { Room.fromString(it) }

    partOne {
        parseInput()
            .filter { it.isReal() }
            .sumOf { it.sectorId }
    } 
    
    partOneTest {
        """
            aaaaa-bbb-z-y-x-123[abxyz]
            a-b-c-d-e-f-g-h-987[abcde]
            not-a-real-room-404[oarel]
            totally-real-room-200[decoy]
        """.trimIndent() shouldOutput 1514
    }

    partTwo {
        parseInput()
            .filter { it.isReal() }
            .first { it.decrypt() == "northpole object storage" }
            .sectorId
    }
}