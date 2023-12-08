package utils

fun <T> List<List<T>>.toGrid() = Grid(this)

fun <T> List<List<T>>.convertRowToColumn(): List<List<T>> {
    if (this.isEmpty()) return emptyList()

    val numRows = this.size
    val numCols = this[0].size

    return List(numCols) { col ->
        List(numRows) { row ->
            this[row][col]
        }
    }
}

fun <T> convertColumnToRow(matrix: List<List<T>>): List<List<T>> {
    if (matrix.isEmpty()) return emptyList()

    val numRows = matrix[0].size
    val numCols = matrix.size

    return List(numRows) { row ->
        List(numCols) { col ->
            matrix[col][row]
        }
    }
}

data class Grid<T>(val grid: List<List<T>>) {

    constructor(size: IntVec2, init: (IntVec2) -> T) : this(
        List(size.x) { x -> List(size.y) { y -> init(IntVec2(x, y)) } }
    )

    val size: IntVec2 get() = IntVec2(grid.size, grid[0].size)

    fun getNeighbors(cell: IntVec2): List<T> = listOfNotNull(
        grid.getOrNull(cell.x - 1)?.getOrNull(cell.y - 1),
        grid.getOrNull(cell.x)?.getOrNull(cell.y - 1),
        grid.getOrNull(cell.x + 1)?.getOrNull(cell.y - 1),
        grid.getOrNull(cell.x - 1)?.getOrNull(cell.y),
        grid.getOrNull(cell.x + 1)?.getOrNull(cell.y),
        grid.getOrNull(cell.x - 1)?.getOrNull(cell.y + 1),
        grid.getOrNull(cell.x)?.getOrNull(cell.y + 1),
        grid.getOrNull(cell.x + 1)?.getOrNull(cell.y + 1)
    )

    fun cellSequence() = (0..<size.x).asSequence().flatMap { x ->
        (0..<size.y).asSequence().map { y ->
            grid[x][y]
        }
    }

    fun cellSequenceWithIndex() = (0..<size.x).asSequence().flatMap { x ->
        (0..<size.y).asSequence().map { y ->
            IntVec2(x, y) to grid[x][y]
        }
    }

    fun map(transform: (T) -> T) = (0..<size.x).map { x ->
        (0..<size.y).map { y ->
            transform(grid[x][y])
        }
    }.let { Grid(it) }

    fun mapIndexed(transform: (IntVec2, T) -> T) = (0..<size.x).map { x ->
        (0..<size.y).map { y ->
            transform(IntVec2(x, y), grid[x][y])
        }
    }.let { Grid(it) }
}