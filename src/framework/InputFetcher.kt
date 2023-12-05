package framework

import java.io.File
import java.net.HttpURLConnection

class InputFetcher(private val day: Day) {

    private val sessionException = Exception("Advent of Code token is missing. Create a '.session' file in the root of the project and paste your Advent of Code token inside")

    private val sessionToken by lazy {
        File(".session").also {
            utils.require(it.isFile, sessionException)
        }.readText().also {
            utils.require(it.isNotBlank(), sessionException)
        }
    }

    fun fetchInput(): DefaultInputProvider {
        if (day.inputFile.isFile) {
//            println()
//            println("Reading input from disk...")
            return DefaultInputProvider(day.inputFile.readText())
        }

//        println("Fetching input from Advent of Code website...")

        val inputText = day.inputUrl
            .openConnection()
            .let { it as HttpURLConnection }
            .apply {
                requestMethod = "GET"
                setRequestProperty("Cookie", "session=$sessionToken")
            }
            .inputStream
            .bufferedReader()
            .readText()
            .trim()

        day.inputFile.apply {
            parentFile.mkdirs()
            writeText(inputText)
        }

        return DefaultInputProvider(inputText)
    }
}