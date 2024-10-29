package year2017.day01

import framework.solution

fun main() = solution(2017, 1, "Inverse Captcha") {

    partOne {
        input
            .asSequence()
            .plus(input[0])
            .map(Char::digitToInt)
            .windowed(2)
            .mapNotNull { (a, b) -> a.takeIf { a == b } }
            .sum()
    }

    partOneTest {
        "1122" shouldOutput 3
        "1111" shouldOutput 4
        "1234" shouldOutput 0
        "91212129" shouldOutput 9
    }
    
    partTwo {
        val sequence = input.asSequence().map(Char::digitToInt)
        val halfLength = input.length / 2
        (sequence.take(halfLength) zip sequence.drop(halfLength))
            .mapNotNull { (a, b) -> a.takeIf { a == b } }
            .sum()
            .times(2)
    }

    partTwoTest {
        "1212" shouldOutput 6
        "1221" shouldOutput 0
        "123425" shouldOutput 4
        "123123" shouldOutput 12
        "12131415" shouldOutput 4
    }
}