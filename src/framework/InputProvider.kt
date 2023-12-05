package framework

abstract class InputProvider {
    abstract val input: String

    val lines by lazy {
        input.lines()
    }

    val lineSequence get() = input.lineSequence()
}

data class DefaultInputProvider(override val input: String): InputProvider()