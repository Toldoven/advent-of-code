package year2015.day21

import framework.InputProvider
import framework.solution
import utils.powerSet

data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)

data class Character(val maxHealth: Int, val damage: Int, val armor: Int, val currentHealth: Int = maxHealth) {

    fun withItems(items: List<Item>) = copy(
        damage = damage + items.sumOf { it.damage },
        armor = armor + items.sumOf { it.armor },
    )

    val isAlive get() = currentHealth > 0

    private fun attack(other: Character): Character {
        if (!isAlive) return other
        val newHealth = other.currentHealth - (damage - other.armor).coerceAtLeast(1)
        return other.copy(currentHealth = newHealth)
    }

    fun fightToDeath(other: Character) = generateSequence(this to other) { (first, second) ->
        if (!first.isAlive || !second.isAlive) return@generateSequence null
        val newSecond = first.attack(second)
        val newFirst = newSecond.attack(first)
        newFirst to newSecond
    }
}

fun main() = solution(2015, 21, "RPG Simulator 20XX") {

    val shopInput = """
        Weapons:    Cost  Damage  Armor
        Dagger        8     4       0
        Shortsword   10     5       0
        Warhammer    25     6       0
        Longsword    40     7       0
        Greataxe     74     8       0
        
        Armor:      Cost  Damage  Armor
        Leather      13     0       1
        Chainmail    31     0       2
        Splintmail   53     0       3
        Bandedmail   75     0       4
        Platemail   102     0       5
        
        Rings:      Cost  Damage  Armor
        Damage +1    25     1       0
        Damage +2    50     2       0
        Damage +3   100     3       0
        Defense +1   20     0       1
        Defense +2   40     0       2
        Defense +3   80     0       3
    """.trimIndent()

    val whitespaceRegex = """\s\s+""".toRegex()

    val (shopWeapons, shopArmor, shopRings) = shopInput.split("\n\n").map { category ->
        category.lines().drop(1).map { itemString ->
            val (name, cost, damage, armor) = itemString.split(whitespaceRegex)
            Item(name, cost.toInt(), damage.toInt(), armor.toInt())
        }
    }

    val possibleArmor = shopArmor + Item("No armor", 0, 0, 0)

    val possibleRings = shopRings.powerSet().filter { it.size <= 2 }.toList()

    val itemVariations = shopWeapons.asSequence().flatMap { weapon ->
        possibleArmor.flatMap { armor ->
            possibleRings.map { rings ->
                listOf(weapon, armor) + rings
            }
        }
    }

    fun InputProvider.parseBoss() = lines
        .map { it.substringAfter(": ").toInt() }
        .let { Character(it[0], it[1], it[2]) }

    val player = Character(100, 0, 0)

    fun Sequence<List<Item>>.fightFightItems(player: Character, boss: Character) = map { items ->
        val character = player.withItems(items)
        val victory = character.fightToDeath(boss).last().first.isAlive
        val cost = items.sumOf { it.cost }
        victory to cost
    }

    partOne {
        itemVariations.fightFightItems(player, parseBoss())
            .filter { (victory, _) -> victory }
            .minOf { (_, cost) -> cost }
    }

    partTwo {
        itemVariations.fightFightItems(player, parseBoss())
            .filter { (victory, _) -> !victory }
            .maxOf { (_, cost) -> cost }
    }
}