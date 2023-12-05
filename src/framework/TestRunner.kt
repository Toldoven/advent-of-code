package framework

sealed class TestResult

data object TestPassed : TestResult()
data class TestWrongOutput(val output: Any?): TestResult()
data class TestException(val cause: Throwable): TestResult()

data class Test(override val input: String, val expectedValue: Any?): InputProvider()

class TestRunner {

    private val testList: MutableList<Test> = mutableListOf()

    infix fun String.shouldOutput(output: Any?) {
        testList.add(Test(this, output))
    }

    infix fun Collection<String>.shouldAllOutput(output: Any?) {
        forEach {
            it shouldOutput output
        }
    }

    private fun getTestResultList(part: Part) = testList.map {
        try {
            val result = part(it)
            if (result.toString() == it.expectedValue.toString()) {
                it to TestPassed
            } else {
                it to TestWrongOutput(result)
            }
        } catch (error: Throwable) {
            it to TestException(error)
        }
    }

    fun test(part: Part) = getTestResultList(part).forEachIndexed { index, (test, result) ->
        val testNumber = index + 1
        when(result) {
            is TestException -> {
                printRed {
                    println("Test $testNumber: ✖ Failed with an exception")
                }
                result.cause.printStackTrace()
            }
            TestPassed -> {
                printGreen {
                    println("Test $testNumber: ✔ Passed")
                }
            }
            is TestWrongOutput -> {
                printRed {
                    println("Test $testNumber: ✖ Wrong output")
                    println(" ⇒ Got: ${result.output}")
                    println(" ⇒ Expected: ${test.expectedValue}")
                }
            }
        }
    }
}