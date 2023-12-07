package year2023.day07

import framework.InputProvider
import framework.solution

typealias WeightMap = Map<Char, Int>

fun List<Char>.toWeightMap(): WeightMap = reversed()
    .withIndex()
    .associate { it.value to it.index }

data class Hand(val value: String, val bid: Int) {

    val score by lazy {
        value.score
    }

    val scoreWithJoker by lazy {
        val countCards = value.groupingBy { it }.eachCount() - 'J'
        val mostCommon = countCards.maxByOrNull { it.value }?.key ?: 'J'
        value.replace('J', mostCommon).score
    }

    private val String.score: Int
        get() {
            val countCards = this.groupingBy { it }.eachCount()
            val highestCount = countCards.maxOf { it.value }
            val pairCount = countCards.values.count { it == 2 }
            return when (highestCount) {
                1 -> 1 // High card
                2 -> if (pairCount == 2) {
                    3 // Two pair
                } else {
                    2 // One pair
                }

                3 -> if (pairCount == 1) {
                    5 // Full house
                } else {
                    4 // Three of a kind
                }

                4 -> 6 // Four of a kind
                5 -> 7 // Five of a kind
                else -> 0 // Should not be possible
            }
        }
}

class HandComparator(private val weightMap: WeightMap, private val withJoker: Boolean) : Comparator<Hand> {

    private val Char.weight get() = weightMap[this] ?: throw IllegalArgumentException("This character is not a card")

    private fun Char.compareToCard(card: Char) = weight.compareTo(card.weight)

    private fun String.compareToHand(hand: String) = zip(hand)
        .map { (card1, card2) -> card1.compareToCard(card2) }
        .firstOrNull { it != 0 } ?: 0

    override fun compare(firstHand: Hand, secondHand: Hand): Int {
        val compareScore = if (withJoker) {
            firstHand.scoreWithJoker.compareTo(secondHand.scoreWithJoker)
        } else {
            firstHand.score.compareTo(secondHand.score)
        }
        return compareScore.takeUnless { it == 0 } ?: firstHand.value.compareToHand(secondHand.value)
    }
}


fun main() = solution(2023, 7, "Camel Cards") {

    fun InputProvider.parseInput() = lines.map { line ->
        val (handValue, bid) = line.split(' ')
        Hand(handValue, bid.toInt())
    }

    fun List<Hand>.solveWith(handComparator: HandComparator) = sortedWith(handComparator)
        .mapIndexed { index, hand -> index + 1 to hand.bid }
        .sumOf { (rank, bid) -> bid * rank }

    partOne {
        val weightMap = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').toWeightMap()
        val handComparator = HandComparator(weightMap, false)
        parseInput().solveWith(handComparator)
    }

    partTwo {
        val weightMap = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').toWeightMap()
        val handComparator = HandComparator(weightMap, true)
        parseInput().solveWith(handComparator)
    }

    val testInput = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 6440
    }

    partTwoTest {
        testInput shouldOutput 5905
    }
}