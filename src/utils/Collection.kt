package utils


/**
 * Returns the power set of a given collection.
 *
 * The power set of a collection is a set of all possible subsets of the collection.
 *
 * For an empty collection, the power set will only contain an empty list. For a non-empty collection,
 * the power set will contain all subsets obtained by either including or excluding each element from the collection.
 *
 * @return a sequence of lists, each representing a subset of the collection
 *
 * @param T the type of elements in the collection
 */
fun <T> Collection<T>.powerSet(): Sequence<List<T>> {
    if (isEmpty()) {
        return sequenceOf(emptyList()) // Return a list containing only an empty list for an empty input
    }
    val element = first() // Select the first element
    val subsets = drop(1).powerSet() // Recursively get subsets for the remaining set
    return subsets + subsets.map { it + element } // Combine subsets without the current element and with the current element
}

/**
 * Returns a list of all combinations of [ofSize] elements from the collection.
 *
 * @param ofSize the size of each combination
 * @return a list of lists representing all combinations
 */
fun <T> Collection<T>.combinations(ofSize: Int): List<List<T>> = when {
    ofSize > this.size -> emptyList()
    ofSize == 0 -> listOf(emptyList())
    else -> this.flatMapIndexed { index, element ->
        this.drop(index + 1).combinations(ofSize - 1).map { listOf(element) + it }
    }
}

// Convert List of Sequences into a Sequence of Lists
fun <T> Collection<Sequence<T>>.zipAll(): Sequence<List<T>> = sequence {
    // Create iterators for each sequence
    val iterators = this@zipAll.map { it.iterator() }

    // Continue yielding lists as long as all iterators can provide a next value
    while (iterators.all { it.hasNext() }) {
        yield(iterators.map { it.next() })
    }
}