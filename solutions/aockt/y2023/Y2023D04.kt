package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D04 : Solution {

    fun parseInput(input: String) = input.lineSequence().map { line ->
        val (winning, yours) = line.substringAfter(':').trim().split(" | ")
        winning.split(' ').mapNotNull { it.trim().toIntOrNull() }.toSet() to
                yours.split(' ').mapNotNull { it.trim().toIntOrNull() }.toSet()
    }

    override fun partOne(input: String) = parseInput(input).map { (winning, yours) ->
        val cardsMatch = yours.filter { winning.contains(it) }
        cardsMatch.fold(0) { acc, _ ->
            if (acc == 0) {
                1
            } else {
                acc * 2
            }
        }
    }.sumOf {
        it
    }

    override fun partTwo(input: String): Int {
        val pepega = parseInput(input).withIndex().toList()

        fun processCard(what: IndexedValue<Pair<Set<Int>, Set<Int>>>): List<IndexedValue<Pair<Set<Int>, Set<Int>>>> {
            val (index, what2) = what
            val (winning, yours) = what2
            val cardsMatch = yours.filter { winning.contains(it) }.size
            return if (cardsMatch != 0) {
                pepega.slice(index.plus(1)..index.plus(1 + cardsMatch))
            } else emptyList()
        }
        
        fun processCards(cards: List<IndexedValue<Pair<Set<Int>, Set<Int>>>>): List<IndexedValue<Pair<Set<Int>, Set<Int>>>> =
            cards.flatMap { card ->
                val newCards = processCard(card)
                when (newCards.size) {
                    0 -> listOf(card)
                    else -> processCards(newCards)
                }
            }


        return processCards(pepega).size
    }
}