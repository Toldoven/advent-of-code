package year2023.day07

import framework.InputProvider
import framework.solution

typealias WeightMap = Map<Char, Int>

fun List<Char>.toWeightMap(): WeightMap = reversed()
    .withIndex()
    .associate { it.value to it.index }

enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND;

    companion object {
        fun fromHandValue(handValue: String): HandType {
            val countCards = handValue.groupingBy { it }.eachCount()
            val highestCount = countCards.maxOf { it.value }
            val pairCount = countCards.values.count { it == 2 }
            return when (highestCount) {
                1 -> HIGH_CARD
                2 -> if (pairCount == 2) TWO_PAIR else ONE_PAIR
                3 -> if (pairCount == 1) FULL_HOUSE else THREE_OF_A_KIND
                4 -> FOUR_OF_A_KIND
                5 -> FIVE_OF_A_KIND
                else -> throw Exception("This hand is not possible: $handValue")
            }
        }
    }
}

data class Hand(val value: String, val bid: Int) {

    val type by lazy {
        HandType.fromHandValue(value)
    }

    val typeWithJoker by lazy {
        val countCards = value.groupingBy { it }.eachCount() - 'J'
        val mostCommon = countCards.maxByOrNull { it.value }?.key ?: 'J'
        val valueWithJoker = value.replace('J', mostCommon)
        HandType.fromHandValue(valueWithJoker)
    }
}

class HandComparator(private val weightMap: WeightMap, private val withJoker: Boolean) : Comparator<Hand> {

    private val Char.cardWeight
        get() = weightMap[this] ?: throw IllegalArgumentException("This character is not a card")

    private fun Hand.compareByCardWeight(other: Hand) = value.zip(other.value)
        .map { (cardA, cardB) -> cardA.cardWeight.compareTo(cardB.cardWeight) }
        .firstOrNull { it != 0 } ?: 0

    override fun compare(firstHand: Hand, secondHand: Hand): Int {
        val compareByHandType = if (withJoker) {
            firstHand.typeWithJoker.compareTo(secondHand.typeWithJoker)
        } else {
            firstHand.type.compareTo(secondHand.type)
        }
        return compareByHandType.takeUnless { it == 0 } ?: firstHand.compareByCardWeight(secondHand)
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