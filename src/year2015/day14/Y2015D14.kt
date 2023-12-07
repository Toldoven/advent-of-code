package year2015.day14

import framework.InputProvider
import framework.solution

private sealed class State
private data class Resting(var timeLeft: Int) : State()
private data class Flying(var staminaLeft: Int) : State()

private data class Deer(
    val name: String,
    val speed: Int,
    val maxStamina: Int,
    val restTime: Int,
    val state: State = Flying(maxStamina),
    val distanceTraveled: Int = 0,
    val score: Int = 0,
) {
    fun inNextSecond(): Deer = when (state) {
        is Resting -> copy(
            state = if (state.timeLeft == 1) {
                Flying(maxStamina)
            } else {
                state.copy(timeLeft = state.timeLeft - 1)
            }
        )

        is Flying -> copy(
            distanceTraveled = distanceTraveled + speed,
            state = if (state.staminaLeft == 1) {
                Resting(restTime)
            } else {
                state.copy(staminaLeft = state.staminaLeft - 1)
            }
        )
    }
}

fun main() = solution(2015, 14, "Reindeer Olympics") {

    val inputRegex = Regex("""^(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.$""")

    fun InputProvider.parseInput() = lines.map { line ->
        val (name, speed, maxStamina, restTime) = inputRegex.matchEntire(line)!!.destructured
        Deer(name, speed.toInt(), maxStamina.toInt(), restTime.toInt())
    }

    fun List<Deer>.raceSequence() = generateSequence(this) { deerList ->
        val newDeerList = deerList.map { it.inNextSecond() }
        val leaderDistance = newDeerList.maxOf { it.distanceTraveled }
        newDeerList.map {
            if (it.distanceTraveled == leaderDistance) {
                it.copy(score = it.score + 1)
            } else {
                it
            }
        }
    }

    partOne {
        parseInput().raceSequence().elementAt(2503).maxOf { it.distanceTraveled }
    }

    partTwo {
        parseInput().raceSequence().elementAt(2503).maxOf { it.score }
    }


    val testInput = """
        Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
    """.trimIndent()

    partOneTest {
        testInput with {
            parseInput().raceSequence().elementAt(1000).maxOf { it.distanceTraveled }
        } shouldOutput 1120
    }

    partTwoTest {
        testInput with {
            parseInput().raceSequence().elementAt(1000).maxOf { it.distanceTraveled }
        } shouldOutput 689
    }
}