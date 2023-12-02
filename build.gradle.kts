import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.kotlin.dsl.KotlinClosure2
import java.net.HttpURLConnection
import java.net.URL
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

plugins {
    kotlin("jvm") version "1.9.20"
    idea
}

kotlin {
    jvmToolchain(17)
}

sourceSets {
    main.configure {
        kotlin.srcDir("$projectDir/solutions")
        kotlin.srcDir("$projectDir/utils")
    }
    test.configure {
        kotlin.srcDir("$projectDir/tests")
        resources.srcDir("$projectDir/inputs")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val aocktVersion = "0.1.0"
    val kotestVersion = "5.5.5"

    implementation("io.github.jadarma.aockt:aockt-core:$aocktVersion")
    implementation("io.github.jadarma.aockt:aockt-test:$aocktVersion")
    implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events = setOf(FAILED, SKIPPED)
        exceptionFormat = FULL
        showStandardStreams = true
        showCauses = true
        showExceptions = true
        showStackTraces = false

        // Prints a summary at the end.
        afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
            if (desc.parent != null) return@KotlinClosure2
            with(result) {
                println(
                    "\nResults: $resultType (" +
                    "$testCount tests, " +
                    "$successfulTestCount passed, " +
                    "$failedTestCount failed, " +
                    "$skippedTestCount skipped" +
                    ")"
                )
            }
        }))
    }
}

// ---

fun getPropertyInt(key: String) = (properties[key] as String?)?.toInt()

val defaultAdventOfCodeYear = getPropertyInt("adventOfCodeYear")

fun now() = LocalDateTime.now(
    // Advent of Code puzzles are published on midnight EST
    ZoneId.of("America/New_York")
)

fun fetchInput(year: Int, day: Int) {
    val session = project.extra["session"]

    val input = URL("https://adventofcode.com/$year/day/$day/input")
        .openConnection()
        .let { it as HttpURLConnection }
        .apply {
            requestMethod = "GET"
            setRequestProperty("Cookie", "session=$session")
        }
        .getInputStream()
        .bufferedReader()
        .readText()

    val paddedDay = day.toString().padStart(2, '0')

    val inputFile = file("inputs/aockt/y$year/d$paddedDay/input.txt")

    inputFile.apply { parentFile.mkdirs() }.writeText(input)
}

fun initDay(year: Int, day: Int, fetchInput: Boolean = true) {

    if (fetchInput) fetchInput(year, day)

    val paddedDay = day.toString().padStart(2, '0')

    val solutionClass = "Y${year}D${paddedDay}"
    val testClass = solutionClass + "Test"

    val solutionFile = file("solutions/aockt/y$year/${solutionClass}.kt")
    val testFile = file("tests/aockt/y$year/${testClass}.kt")

    solutionFile
        .takeUnless { it.isFile }
        ?.apply { parentFile.mkdirs() }
        ?.writeText("""
            package aockt.y$year
    
            import io.github.jadarma.aockt.core.Solution
    
            object $solutionClass : Solution {
                override fun partOne(input: String): Any {
                    return 0
                }
            }
        """.trimIndent())

    testFile
        .takeUnless { it.isFile }
        ?.apply { parentFile.mkdirs() }
        ?.writeText("""
            package aockt.y$year
    
            import io.github.jadarma.aockt.test.AdventDay
            import io.github.jadarma.aockt.test.AdventSpec
    
            @AdventDay($year, $day)
            class $testClass : AdventSpec<$solutionClass>({
                
            })
        """.trimIndent())

    println("""
        ─────────────────────────────────────────────
        Year $year, Day $day
        $solutionFile
        $testFile
        ─────────────────────────────────────────────
    """.trimIndent())
}

tasks.register("setupSession") {
    group = "adventofcode"
    val exception = Exception("Please enter your session token in .session file")
    doLast {
        val file = file(".session")
        if (!file.isFile) {
            file.createNewFile()
            throw exception
        }
        val text = file.readText()
        if (text.isBlank()) {
            throw exception
        }
        project.extra["session"] = text
        return@doLast
    }
}

fun aocDateTimeForYear(year: Int): LocalDateTime = LocalDateTime.of(year, Month.DECEMBER, 1, 0, 0)

fun Duration.pretty(): String {
    val months = toDays() / 30
    val days = toDays()
    val hours = toHoursPart().toLong()
    val minutes = toMinutesPart().toLong()

    fun pluralize(ofWhat: String, howMany: Long) = when (howMany) {
        in -1..1 -> "$howMany $ofWhat"
        else -> "$howMany ${ofWhat}s"
    }

    return when {
        months > 0 -> pluralize("month", months)
        days > 0 -> pluralize("day", days)
        else -> "${pluralize("hour", hours)} and ${pluralize("minute", minutes)}"
    }
}

tasks.register("initToday") {
    group = "adventofcode"
    dependsOn("setupSession")
    doLast {
        val now = now()
        val day = now.dayOfMonth
        if (now.month != Month.DECEMBER || day < 1 || day > 25) {
            val nextAoc = aocDateTimeForYear(now.year)
                .takeUnless { now.isAfter(it) }
                ?: aocDateTimeForYear(now.year + 1)
            val untilNextAoc = Duration.between(now, nextAoc)
            throw Exception("Today is not the day of Advent of Code. AOC will begin in: ${untilNextAoc.pretty()}")
        }
        val year = now.year
        initDay(year, day)
    }
}

tasks.register("initTomorrow") {
    group = "adventofcode"
    doLast {
        val now = now().plusDays(1)
        val day = now.dayOfMonth
        if (now.month != Month.DECEMBER || day < 1 || day > 25) {
            val nextAoc = aocDateTimeForYear(now.year)
                .takeUnless { now.isAfter(it) }
                ?: aocDateTimeForYear(now.year + 1)
            val untilNextAoc = Duration.between(now, nextAoc)
            throw Exception("Tomorrow is not the day of Advent of Code. AOC will begin in: ${untilNextAoc.pretty()}")
        }
        val year = now.year
        initDay(year, day, fetchInput = false)
    }
}

(1..25).forEach { day ->
    tasks.register("initDay$day") {
        group = "adventofcode"
        dependsOn("setupSession")
        doLast {
            val now = now()
            val year = defaultAdventOfCodeYear ?: run {
                println("Year is not specified, falling back to current year...")
                now.year
            }
            initDay(year, day)
        }
    }
}