package utils

fun <T> Sequence<T>.cycle() = generateSequence { this }.flatten()

fun <T> Iterable<T>.cycle() = asSequence().cycle()

fun CharSequence.cycle() = asSequence().cycle()

fun <T> Sequence<T>.repeat(n: Int) = sequence {
    repeat(n) {
        yieldAll(this@repeat)
    }
}

fun <T> Iterable<T>.repeat(times: Int) = asSequence().repeat(times)

fun CharSequence.repeat(times: Int) = asSequence().repeat(times)

operator fun <T> Collection<T>.times(times: Int) = repeat(times).toList()

operator fun CharSequence.times(times: Int) = repeat(times).joinToString("")