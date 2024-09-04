package year2016.day01

import framework.InputProvider
import framework.solution
import utils.Direction
import utils.IntVec2

enum class Turn {
    CLOCKWISE,
    COUNTER_CLOCKWISE,
}

fun Direction.turn(turn: Turn) = when (turn) {
    Turn.COUNTER_CLOCKWISE -> turnCounterClockwise
    Turn.CLOCKWISE -> turnClockwise
}

data class State(val direction: Direction, val position: IntVec2) {
    fun turnAndMove(turn: Turn, distance: Int): State {
        val newDirection = direction.turn(turn)
        val newPosition = position.moveInDirection(newDirection, distance)
        return State(newDirection, newPosition)
    }

    fun runningTurnAndMove(turn: Turn, distance: Int): Sequence<State> {
        val newDirection = direction.turn(turn)

        val sequence = generateSequence(position) { it.moveInDirection(newDirection) }
            .drop(1) // Skip the initial position, we only need the new ones
            .take(distance)
            .map { newPosition -> State(newDirection, newPosition) }

        return sequence
    }
}

fun main() = solution(2016, 1, "No Time for a Taxicab") {

    fun InputProvider.parseInput() = input.split(", ").map {
        val distance = it.drop(1).toInt()
        val turn = when (it[0]) {
            'R' -> Turn.CLOCKWISE
            'L' -> Turn.COUNTER_CLOCKWISE
             else -> throw IllegalArgumentException("Expected turn to be either to the left or to the right")
        }
        turn to distance
    }

    val initialState = State(Direction.UP, IntVec2.ZERO)

    partOne {
        val finalState = parseInput().fold(initialState) { state, (turn, distance) ->
            state.turnAndMove(turn, distance)
        }
        initialState.position.manhattanDistanceTo(finalState.position)
    }

    partOneTest {
        "R2, L3" shouldOutput 5
        "R2, R2, R2" shouldOutput 2
        "R5, L5, R5, R3" shouldOutput 12
    }

    partTwo {
        val visited = mutableSetOf<IntVec2>()
        val finalPosition = parseInput()
            .asSequence()
            .scan(listOf(initialState)) { newStates, (turn, distance) ->
                val currentState = newStates.last()
                currentState.runningTurnAndMove(turn, distance).toList()
            }
            .flatten()
            .map { it.position }
            .first { !visited.add(it) }

        initialState.position.manhattanDistanceTo(finalPosition)
    }

    partTwoTest {
        "R8, R4, R4, R8" shouldOutput 4
    }
}