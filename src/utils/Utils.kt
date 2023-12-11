package utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

private fun splitOnceInner(split: List<String>) = if (split.size == 2) split[0] to split[1] else null

fun CharSequence.splitOnceOrNull(vararg delimiters: String, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase, limit = 2).let(::splitOnceInner)

fun CharSequence.splitOnceOrNull(vararg delimiters: Char, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase, limit = 2).let(::splitOnceInner)

fun CharSequence.splitOnce(vararg delimiters: String, ignoreCase: Boolean = false) =
    splitOnceOrNull(*delimiters, ignoreCase = ignoreCase) ?: throw IllegalArgumentException()

fun CharSequence.splitOnce(vararg delimiters: Char, ignoreCase: Boolean = false) =
    splitOnceOrNull(*delimiters, ignoreCase = ignoreCase) ?: throw IllegalArgumentException()


fun CharSequence.splitFromEnd(vararg delimiters: Char, ignoreCase: Boolean = false, limit: Int = 0) =
    reversed().split(*delimiters, ignoreCase = ignoreCase, limit = limit).map { it.reversed() }.reversed()

fun Iterable<Int>.product() = reduce { acc, cur -> acc * cur }

fun Iterable<Long>.product() = reduce { acc, cur -> acc * cur }

inline fun <T> Iterable<T>.productOf(transform: (T) -> Int) = map(transform).product()

inline fun <T, V> Iterable<T>.indexedAssociateWith(valueSelector: (Int, T) -> V) =
    withIndex().map { (index, key) -> key to valueSelector(index, key) }

inline fun <T, V> Iterable<T>.indexedAssociateWith(valueSelector: (Int) -> V) =
    withIndex().map { (index, key) -> key to valueSelector(index) }

inline fun <T, V> Sequence<T>.indexedAssociateWith(crossinline valueSelector: (Int, T) -> V) =
    withIndex().map { (index, key) -> key to valueSelector(index, key) }

inline fun <T, V> Sequence<T>.indexedAssociateWith(crossinline valueSelector: (Int) -> V) =
    withIndex().map { (index, key) -> key to valueSelector(index) }

inline fun <V> CharSequence.indexedAssociateWith(valueSelector: (Int, Char) -> V) =
    withIndex().map { (index, key) -> key to valueSelector(index, key) }

inline fun <V> CharSequence.indexedAssociateWith(valueSelector: (Int) -> V) =
    withIndex().map { (index, key) -> key to valueSelector(index) }

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

inline fun <reified T : Enum<T>> String.toEnum(): T =
    enumValues<T>().first { it.name.equals(this.replace(' ', '_'), ignoreCase = true) }

data class IntVec2(val x: Int, val y: Int) {

    val adjacent4Way get() = Direction.entries.map { moveInDirection(it) }

    val adjacent8Way
        get() = adjacent4Way + listOf(
            copy(x = x - 1, y = y - 1),
            copy(x = x - 1, y = y + 1),
            copy(x = x + 1, y = y - 1),
            copy(x = x + 1, y = y + 1),
        )

    fun moveInDirection(direction: Direction, amount: Int = 1) = when (direction) {
        Direction.UP -> copy(y = y - amount)
        Direction.DOWN -> copy(y = y + amount)
        Direction.LEFT -> copy(x = x - amount)
        Direction.RIGHT -> copy(x = x + amount)
    }

    fun isPointInPolygon(polygon: List<IntVec2>) = polygon.windowed(2).count { (previous, current) ->
        previous.y > y != current.y > y && x < (current.x - previous.x) * (y - previous.y) / (current.y - previous.y) + previous.x
    } % 2 != 0

    fun manhattanDistanceTo(other: IntVec2) = (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue
}

data class LongVec2(val x: Long, val y: Long) {

    fun manhattanDistanceTo(other: LongVec2) = (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue

}


fun String.toIntVec2() =
    this.splitOnce(',').let { IntVec2(it.first.toInt(), it.second.toInt()) }


class SquareGrid<T>(size: Int = 1000, init: (Int) -> T) {
    val grid = List(1000) { MutableList(1000, init) }
}

fun <K, V> Iterable<Map<K, V>>.flatten() = map { it.toList() }.flatten()

inline fun <A, B, R> Pair<A, B>.letFirst(transform: (A) -> R) = let { transform(it.first) to it.second }

inline fun <A, B, R> Pair<A, B>.letSecond(transform: (B) -> R) = let { it.first to transform(it.second) }

inline fun <K, V> MutableMap<K, V>.defaultAndModify(key: K, default: V, modify: (V) -> V) {
    this[key] = getOrDefault(key, default).let(modify)
}

fun String.isInt() = trim().all { it.isDigit() } && isNotEmpty()

fun <T> List<T>.safeSlice(indices: IntRange) = slice(
    max(indices.first, 0)..min(indices.last, size - 1),
)

fun CharSequence.safeSlice(indices: IntRange) = slice(
    max(indices.first, 0)..min(indices.last, length - 1),
)

fun <T> List<T>.safeSublist(indices: IntRange) = subList(
    max(indices.first, 0), min(indices.last, size - 1) + 1,
)

fun <T, R> T.letIf(condition: Boolean, transform: (T) -> R) =
    if (condition) transform(this) else null

fun <T, R> T.letIfElse(condition: Boolean, transformTrue: (T) -> R, transformFalse: (T) -> R) =
    if (condition) transformTrue(this) else transformFalse(this)


@OptIn(ExperimentalContracts::class)
fun require(condition: Boolean, exception: Throwable) {
    contract {
        returns() implies condition
    }
    if (!condition) {
        throw exception
    }
}


fun <T> T.dbg(): T {
    println(this)
    return this
}

fun String.parseNumbersLong(vararg delimiters: String, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase).filter { it.isNotBlank() }.map { it.trim().toLong() }

fun String.parseNumbersLong(vararg delimiters: Char, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase).filter { it.isNotBlank() }.map { it.trim().toLong() }

fun String.parseNumbersInt(vararg delimiters: String, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase).filter { it.isNotBlank() }.map { it.trim().toLong() }

fun String.parseNumbersInt(vararg delimiters: Char, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase).filter { it.isNotBlank() }.map { it.trim().toInt() }