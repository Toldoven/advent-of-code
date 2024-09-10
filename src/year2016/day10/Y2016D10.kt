package year2016.day10

import framework.InputProvider
import framework.solution
import utils.letFirst
import utils.letSecond
import utils.productOf
import java.util.LinkedList

enum class ChipReceiver { BOT, OUTPUT }

sealed class Command(open val bot: Int) {

    abstract fun execute(state: State)

    abstract fun canExecute(state: State): Boolean

    data class Take(override val bot: Int, val value: Int): Command(bot) {
        override fun execute(state: State) {
            state.giveToBot(bot, value)
        }

        override fun canExecute(state: State): Boolean {
            val botState = state.getBot(bot)
            return botState.first == null || botState.second == null
        }
    }

    data class Give(
        override val bot: Int,
        val lowReceiver: Pair<ChipReceiver, Int>,
        val highReceiver: Pair<ChipReceiver, Int>,
    ): Command(bot) {
        override fun execute(state: State) {
            val (first, second) = state.getBot(bot)

            if (first == null || second == null) {
                throw IllegalStateException("Can't execute the command")
            }

            val (lowValue, highValue) = listOf(first, second).sortedBy { it }

            if (lowValue == 17 && highValue == 61) {
                assert(state.partOneResult == null) { "There should be only one bot that compares 17 and 61" }
                state.partOneResult = bot
            }

            state.giveTo(lowReceiver, lowValue)
            state.giveTo(highReceiver, highValue)
        }

        override fun canExecute(state: State): Boolean {
            val bot = state.botMap[bot] ?: return false
            return bot.first != null && bot.second != null
        }
    }
}

data class State(
    // Key is Output Index
    val outputMap: MutableMap<Int, Int> = mutableMapOf(),
    // Key is Bot Index
    val botMap: MutableMap<Int, Pair<Int?, Int?>> = mutableMapOf(),
    // Key is Bot Index
    val botCommandCache: MutableMap<Int, MutableList<Command>> = mutableMapOf(),

) {
    var partOneResult: Int? = null

    val partTwoResult by lazy {
        (0..2).productOf { outputMap[it] ?: 0 }
    }

    fun getBot(bot: Int) = botMap.computeIfAbsent(bot) { null to null }

    fun giveToBot(bot: Int, value: Int) {
        val botState = getBot(bot)

        val newBotState = when {
            botState.first == null -> botState.letFirst { value }
            botState.second == null -> botState.letSecond { value }
            else -> throw IllegalStateException("Can't execute command")
        }

        botMap[bot] = newBotState
    }

    private fun giveToOutput(output: Int, value: Int) {
        val result = outputMap.put(output, value)
        assert(result == null) { "Output can only hold one value "}
    }

    fun giveTo(receiver: Pair<ChipReceiver, Int>, value: Int) = when (receiver.first) {
        ChipReceiver.BOT -> giveToBot(receiver.second, value)
        ChipReceiver.OUTPUT -> giveToOutput(receiver.second, value)
    }

    fun processCommands(commands: List<Command>) {
        val queue = LinkedList(commands)

        while (queue.isNotEmpty()) {
            val index = queue.indexOfFirst { it.canExecute(this) }
            val command = queue.removeAt(index)
            command.execute(this)
        }
    }

}


fun main() = solution(2016, 10, "Balance Bots") {

    fun String.toChipReceiver() = when (this) {
        "bot" -> ChipReceiver.BOT
        "output" -> ChipReceiver.OUTPUT
        else -> throw IllegalArgumentException("Unknown chip receiver: $this")
    }

    fun InputProvider.parseInput() = lines.map { line ->
        val split = line.split(' ')

        when (val commandString = split[0]) {
            "value" ->  Command.Take(split[5].toInt(), split[1].toInt())
            "bot" -> Command.Give(
                split[1].toInt(),
                (split[5].toChipReceiver() to split[6].toInt()),
                (split[10].toChipReceiver() to split[11].toInt()),
            )
            else -> throw IllegalArgumentException("Unknown command: $commandString")
        }
    }


    partOne {
        val commands = parseInput()
        val state = State()
        state.processCommands(commands)
        state.partOneResult
    } 
    
    partTwo {
        val commands = parseInput()
        val state = State()
        state.processCommands(commands)
        state.partTwoResult
    }
}