package framework

fun prettyLine(char: Char, text: String? = null, lineLength: Int = 64): String {
    if (text == null) {
        return char.toString().repeat(lineLength)
    }
    val halfLength = (lineLength - text.length - 2) / 2
    val repeated = char.toString().repeat(halfLength)
    return "$repeated $text $repeated"
}

fun prettyFrame(text: String, frameLength: Int = 64): String {
    val middleBorder = "━".repeat(frameLength - 2)
    val oddPadding = if (text.length % 2 != 0) " " else ""
    val middleSpace = " ".repeat((frameLength - text.length - 2) / 2)
    return buildString {
        append("┏$middleBorder┓\n")
        append("┃$middleSpace$text$middleSpace$oddPadding┃\n")
        append("┗$middleBorder┛")
    }
}

const val ansi_reset = "\u001B[0m"
const val ansi_red = "\u001B[31m"
const val ansi_green = "\u001B[32m"
const val ansi_yellow = "\u001B[93m"

private inline fun printColor(color: String, function: () -> Unit) {
    print(color)
    function()
    print(ansi_reset)
}

fun printRed(function: () -> Unit) = printColor(ansi_red, function)
fun printGreen(function: () -> Unit) = printColor(ansi_green, function)
fun printYellow(function: () -> Unit) = printColor(ansi_yellow, function)
