package year2016.day17

import framework.solution
import utils.Direction
import utils.IntVec2
import utils.md5

fun Char.isOpenDoor() = when (this) {
    'b', 'c', 'd', 'e', 'f' -> true
    else -> false
}

fun List<Direction>.asHistoryString() = joinToString("") {
    when(it) {
        Direction.UP -> "U"
        Direction.DOWN -> "D"
        Direction.LEFT -> "L"
        Direction.RIGHT -> "R"
    }
}

data class Path(val current: IntVec2, val history: List<Direction> = emptyList()) {
    fun walkInDirection(direction: Direction) = Path(current.moveInDirection(direction), history + direction)
}


data class Rooms(val passcode: String) {

    private fun Path.availableDirections(): List<Direction> {
        val isOpen = "$passcode${history.asHistoryString()}"
            .md5()
            .take(4)
            .map { it.isOpenDoor() }

        return Direction.entries.zip(isOpen)
            .filter { it.second }
            .map { it.first }
            .filter {
                val index = current.moveInDirection(it)
                index.x in (0..3) && index.y in (0..3)
            }
    }

    private fun bfsSequence() = generateSequence(sequenceOf(Path(IntVec2(0, 0)))) { pathList ->
        pathList.flatMap { path ->
            path.availableDirections().map {
                path.walkInDirection(it)
            }
        }
    }

    fun findShortest() = bfsSequence().firstNotNullOf { pathList ->
        pathList.find { it.current == IntVec2(3, 3) }
    }


    fun findLongest(path: Path = Path(IntVec2(0, 0))): Int? {
        if (path.current == IntVec2(3, 3)) {
            return path.history.size
        }

        return path
            .availableDirections()
            .mapNotNull { findLongest(path.walkInDirection(it)) }
            .maxOrNull()
    }
}

fun main() = solution(2016, 17, "Two Steps Forward") {

    partOne {
        Rooms(input).findShortest().history.asHistoryString()
    } 
    
    partOneTest {
        "ihgpwlah" shouldOutput "DDRRRD"
        "kglvqrro" shouldOutput "DDUDRLRRUDRD"
        "ulqzkmiv" shouldOutput "DRURDRUDDLLDLUURRDULRLDUUDDDRR"
    }

    partTwo {
        Rooms(input).findLongest()
    }

    partTwoTest {
        "ihgpwlah" shouldOutput 370
        "kglvqrro" shouldOutput 492
        "ulqzkmiv" shouldOutput 830
    }
}