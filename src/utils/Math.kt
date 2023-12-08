package utils

import kotlin.math.abs

tailrec fun greatCommonDivider(a: Long, b: Long): Long = if (b == 0L) a else greatCommonDivider(b, a % b)

fun leastCommonMultiplier(a: Long, b: Long): Long =
    if (a == 0L || b == 0L) 0 else abs(a * b) / greatCommonDivider(a, b)

fun <T : Number> Iterable<T>.leastCommonMultiplier(): Long =
    map { it.toLong() }.reduce { acc, num -> leastCommonMultiplier(acc, num) }