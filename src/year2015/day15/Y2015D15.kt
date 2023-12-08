package year2015.day15

import framework.InputProvider
import framework.solution
import utils.product

typealias IngredientList = List<Map<String, Int>>

fun main() = solution(2015, 15, "Science for Hungry People") {

    fun InputProvider.parseInput(): IngredientList = lines.map { line ->
        line.substringAfter(": ").split(", ").associate {
            val (statName, value) = it.split(" ")
            statName to value.toInt()
        }
    }

    fun IngredientList.getCookieProperties(ingredientCountList: List<Int>) = zip(ingredientCountList)
        .flatMap { (properties, count) -> properties.mapValues { it.value * count }.entries }
        .groupBy({ it.key }, { it.value })
        .mapValues { it.value.sum().coerceAtLeast(0) }

    fun IngredientList.findBestRecipe(
        ingredientsLeft: Int = this.size,
        ingredientCountList: List<Int> = emptyList(),
        spoonsLeft: Int = 100,
        calorieTarget: Int? = null,
    ): Int = if (ingredientsLeft == 1) {
        val cookieProperties = getCookieProperties(ingredientCountList + spoonsLeft)
        if (calorieTarget != null && cookieProperties["calories"] != calorieTarget) {
            0
        } else {
            (cookieProperties - "calories").values.product()
        }
    } else {
        (0..spoonsLeft).maxOf {
            findBestRecipe(ingredientsLeft - 1, ingredientCountList + it, spoonsLeft - it, calorieTarget)
        }
    }

    partOne {
        parseInput().findBestRecipe()
    }

    partTwo {
        parseInput().findBestRecipe(calorieTarget = 500)
    }

    val testInput = """
        Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
        Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 62842880
    }

    partTwoTest {
        testInput shouldOutput 57600000
    }
}