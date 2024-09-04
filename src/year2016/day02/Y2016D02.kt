package year2016.day02

import framework.InputProvider
import framework.solution
import utils.Direction
import utils.Grid
import utils.IntVec2
import utils.convertRowToColumn

typealias Keypad = Grid<Char?>

fun main() = solution(2016, 2, "Bathroom Security") {

    fun InputProvider.parseInput() = lines.map { line ->
        line.map { when (it) {
            'U' -> Direction.UP
            'D' -> Direction.DOWN
            'L' -> Direction.LEFT
            'R' -> Direction.RIGHT
            else -> throw IllegalArgumentException("Wrong direction")
        }}
    }

    fun String.parseKeypad(): Keypad = lines().map { line -> line
        // Skip every other element "1 2 3" -> "123"
        .filterIndexed { index, _ -> index % 2 == 0 }
        .map { it.takeIf { it != ' ' } }
    }
        .convertRowToColumn()
        .let { Grid(it) }


    fun Keypad.getPasswordFromInstructions(instructions: List<List<Direction>>, initialIndex: IntVec2) =
        instructions.runningFold(initialIndex) { startIndex, instructionsLine ->
            instructionsLine.fold(startIndex) { currentIndex, direction ->
                val newIndex = currentIndex.moveInDirection(direction)
                if (getOrNull(newIndex) != null) newIndex else currentIndex
            }
        }
            .drop(1)
            .map { get(it) }
            .joinToString("")

    partOne {
        val keypad = """
            1 2 3
            4 5 6
            7 8 9
        """.trimIndent().parseKeypad()

        keypad.getPasswordFromInstructions(
            parseInput(), IntVec2(1, 1)
        )
    } 
    
    partOneTest {
        """
            ULL
            RRDDD
            LURDL
            UUUUD
        """.trimIndent() shouldOutput 1985
    }

    partTwo {
        val keypad = """
                1    
              2 3 4  
            5 6 7 8 9
              A B C  
                D    
        """.trimIndent().parseKeypad()

        keypad.getPasswordFromInstructions(
            parseInput(), IntVec2(0, 2)
        )
    }

    partTwoTest {
        """
            ULL
            RRDDD
            LURDL
            UUUUD
        """.trimIndent() shouldOutput "5DB3"
    }
}