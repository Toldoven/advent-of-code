package utils

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    val turnClockwise
        get() = when (this) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> UP
            RIGHT -> DOWN
        }

    val turnCounterClockwise
        get() = when (this) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> DOWN
            RIGHT -> UP
        }
}