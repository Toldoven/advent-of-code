package aockt

private fun splitOnceInner(split: List<String>) = if (split.size == 2) split[0] to split[1] else null

fun CharSequence.splitOnceOrNull(vararg delimiters: String, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase, limit = 2).let(::splitOnceInner)

fun CharSequence.splitOnceOrNull(vararg delimiters: Char, ignoreCase: Boolean = false) =
    split(*delimiters, ignoreCase = ignoreCase, limit = 2).let(::splitOnceInner)

fun CharSequence.splitOnce(vararg delimiters: String, ignoreCase: Boolean = false) =
    splitOnceOrNull(*delimiters, ignoreCase = ignoreCase) ?: throw IllegalArgumentException()

fun CharSequence.splitOnce(vararg delimiters: Char, ignoreCase: Boolean = false) =
    splitOnceOrNull(*delimiters, ignoreCase = ignoreCase) ?: throw IllegalArgumentException()
