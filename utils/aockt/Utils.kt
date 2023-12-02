package aockt

import java.math.BigInteger
import java.security.MessageDigest

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

fun Iterable<Int>.product() = reduce { acc, cur -> acc * cur}

inline fun <T, V> Iterable<T>.indexedAssociateWith(valueSelector: (Int, T) -> V) =
    withIndex().map { (index, key) -> key to valueSelector(index, key) }

inline fun <T, V> Iterable<T>.indexedAssociateWith(valueSelector: (Int) -> V) =
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

data class IntVec2(val x: Int, val y: Int)

fun String.toIntVec2() =
    this.splitOnce(',').let { IntVec2(it.first.toInt(), it.second.toInt()) }


class SquareGrid<T> (size: Int = 1000, init: (Int) -> T) {
    val grid = List(1000) { MutableList(1000, init) }
}


fun <T> T.dbg(): T {
    println(this)
    return this
}