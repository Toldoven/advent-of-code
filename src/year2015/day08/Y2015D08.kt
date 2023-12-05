package year2015.day08

import framework.solution

fun main() = solution(2015, 8, "Matchsticks") {

    val partOneRegex = Regex("""\\"|\\{2}|\\x[0-9a-f]{2}""")

    partOne {
        lines.sumOf {
            val decoded = it.trim('"').replace(partOneRegex, "_")
            it.length - decoded.length
        }
    }

    partTwo {
        lines.sumOf {
            val encoded = it.replace("\\", "\\\\").replace("\"", "\\\"")
            encoded.length + 2 - it.length
        }
    }

    val testInput = """
        ""
        "abc"
        "aaa\"aaa"
        "\x27"
    """.trimIndent()

    partOneTest {
        testInput shouldOutput 12
    }

    partTwoTest {
        testInput shouldOutput 19
    }
}