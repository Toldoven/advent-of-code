package year2016.day15

import framework.InputProvider
import framework.solution
import utils.zipAll

data class Disc(val positions: Int, val currentPosition: Int) {
    private fun advance() =
        copy(currentPosition = (currentPosition + 1) % positions)

    fun asSequence() = generateSequence(this) { it.advance() }

    companion object {
        private val regex = Regex("""Disc #\d+ has (\d+) positions; at time=0, it is at position (\d+).""")

        fun fromString(string: String): Disc {
            val (positions, currentPosition) = regex.matchEntire(string)?.destructured
                ?: throw IllegalArgumentException("String doesn't match disc: $string")

            return Disc(positions.toInt(), currentPosition.toInt())
        }
    }
}

fun main() = solution(2016, 15, "Timing is Everything") {

    fun InputProvider.parseInput() = lines.map { Disc.fromString(it) }


    fun List<Disc>.findTiming() = mapIndexed { index, disc ->
        // Offset every disc so we get its position at the moment it matches the ball
        disc.asSequence().drop(index + 1)
    }
        .zipAll()
        .indexOfFirst { disc ->
            // Find a time when every disc is at zero
            disc.all { it.currentPosition == 0 }
        }

    partOne {
        parseInput().findTiming()
    }

    partTwo {
        parseInput()
            .plusElement(Disc(11, 0))
            .findTiming()
    }
    
    partOneTest {
        """
            Disc #1 has 5 positions; at time=0, it is at position 4.
            Disc #2 has 2 positions; at time=0, it is at position 1.
        """.trimIndent() shouldOutput 5
    }
}