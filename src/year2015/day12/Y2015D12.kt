package year2015.day12

import framework.solution
import kotlinx.serialization.json.*

fun main() = solution(2015, 12, "JSAbacusFramework.io") {

    fun parseElement(jsonElement: JsonElement, redCheck: Boolean): Int = when (jsonElement) {
        is JsonPrimitive -> jsonElement.intOrNull ?: 0
        is JsonArray -> jsonElement.sumOf { parseElement(it, redCheck) }
        is JsonObject -> jsonElement.values
            .takeIf { values ->
                !redCheck || values.all { it != JsonPrimitive("red") }
            }
            ?.sumOf { parseElement(it, redCheck) } ?: 0
    }

    partOne {
        parseElement(Json.parseToJsonElement(input), false)
    }

    partTwo {
        parseElement(Json.parseToJsonElement(input), true)
    }

    partOneTest {
        listOf("[1,2,3]", "{\"a\":2,\"b\":4}") shouldAllOutput 6
        listOf("[[[3]]]", "{\"a\":{\"b\":4},\"c\":-1}") shouldAllOutput 3
        listOf("{\"a\":[-1,1]}", "[-1,{\"a\":1}]", "[]", "{}") shouldAllOutput 0
    }
    
    partTwoTest {
        "[1,2,3]" shouldOutput 6
        "[1,{\"c\":\"red\",\"b\":2},3]" shouldOutput 4
        "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}" shouldOutput 0
        "[1,\"red\",5]" shouldOutput 6
    }
}