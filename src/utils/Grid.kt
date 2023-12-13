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

    fun getOrNull(cell: IntVec2) = grid.getOrNull(cell.x)?.getOrNull(cell.y)

    fun getAdjacent8Way(cell: IntVec2): List<T> = cell.adjacent8Way.mapNotNull { getOrNull(it) }

    fun getAdjacent4Way(cell: IntVec2): List<T> = cell.adjacent4Way.mapNotNull { getOrNull(it) }

    fun columns() = (0..<size.x).asSequence().map { x ->
        grid[x]
    }

    fun rows() = (0..<size.y).asSequence().map { y ->
        (0..<size.x).map { x ->
            grid[x].elementAt(y)
        }
    }

    fun <R> flatMapColumns(transform: (List<T>) -> List<List<R>>) =
        columns().flatMap(transform).toList().toGrid()

    fun <R> flatMapRows(transform: (List<T>) -> List<List<R>>) =
        rows().flatMap(transform).toList().convertRowToColumn().toGrid()

    fun <R> flatMapColumnsIndexed(transform: (IndexedValue<List<T>>) -> List<List<R>>) =
        columns().withIndex().flatMap(transform).toList().toGrid()

    fun <R> flatMapRowsIndexed(transform: (IndexedValue<List<T>>) -> List<List<R>>) =
        rows().withIndex().flatMap(transform).toList().convertRowToColumn().toGrid()

    fun asSequence() = (0..<size.x).asSequence().flatMap { x ->
        (0..<size.y).asSequence().map { y ->
            grid[x][y]
        }
    }

    fun asIndexedSequence() = (0..<size.x).asSequence().flatMap { x ->
        (0..<size.y).asSequence().map { y ->
            IntVec2(x, y) to grid[x][y]
        }
    }

    val indices
        get() = (0..<size.x).flatMap { x ->
            (0..<size.y).map { y -> IntVec2(x, y) }
        }

    fun map(transform: (T) -> T) = (0..<size.x).map { x ->
        (0..<size.y).map { y ->
            transform(grid[x][y])
        }
    }.let { Grid(it) }

    fun <R> mapIndexed(transform: (IntVec2, T) -> R) = (0..<size.x).map { x ->
        (0..<size.y).map { y ->
            transform(IntVec2(x, y), grid[x][y])
        }
    }.let { Grid(it) }

    override fun toString(): String {
        return rows().map { it.joinToString("") }.joinToString("\n", prefix = "Grid:\n")
    }
}