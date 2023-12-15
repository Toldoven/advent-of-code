package year2023.day15

import framework.solution

private data class Lens(val label: String, val focalLength: Int)

private class Box(val lenses: MutableList<Lens> = mutableListOf())

private sealed interface Operation {
    val label: String
    fun applyToBox(box: Box)
}

private data class Remove(override val label: String) : Operation {
    override fun applyToBox(box: Box) {
        box.lenses.removeIf { it.label == label }
    }
}

private data class Insert(override val label: String, val focalLength: Int) : Operation {
    override fun applyToBox(box: Box) {
        val newLens = Lens(label, focalLength)
        val existingLens = box.lenses.indexOfFirst { it.label == label }
        if (existingLens == -1) {
            box.lenses.add(newLens)
        } else {
            box.lenses[existingLens] = newLens
        }
    }
}

fun main() = solution(2023, 15, "Lens Library") {

    fun String.hash() = fold(0) { acc, char ->
        acc.plus(char.code).times(17).rem(256)
    }

    partOne {
        input.split(',').sumOf { it.hash() }
    }

    val regex = """(\w+)([-=])(\d*)""".toRegex()

    partTwo {
        val operations = input.split(',').map {
            val (label, operation, focalLength) = regex.matchEntire(it)?.destructured
                ?: throw IllegalArgumentException("'$it' doesn't match regex")
            when (operation) {
                "-" -> Remove(label)
                "=" -> Insert(label, focalLength.toInt())
                else -> throw IllegalArgumentException("Unknown operation: $operation")
            }
        }

        val boxes = mutableMapOf<Int, Box>()

        for (operation in operations) {
            val boxNumber = operation.label.hash()
            val box = boxes.computeIfAbsent(boxNumber) { Box() }
            operation.applyToBox(box)
        }

        boxes.entries.sumOf { (boxNumber, box) ->
            box.lenses.withIndex().sumOf { (lensIndex, lens) ->
                (boxNumber + 1) * (lensIndex + 1) * lens.focalLength
            }
        }
    }

    val testInput = """rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"""

    partOneTest {
        testInput shouldOutput 1320
    }

    partTwoTest {
        testInput shouldOutput 145
    }
}