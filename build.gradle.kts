import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    idea
}

kotlin {
    jvmToolchain(17)
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val arrowVersion = "1.2.0"
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
//    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
//    implementation("com.github.ajalt.mordant:mordant:2.2.0")
}

// ---

fun getPropertyInt(key: String) = (properties[key] as String?)?.toInt()

val defaultAdventOfCodeYear = getPropertyInt("adventOfCodeYear")

fun now(): LocalDateTime = LocalDateTime.now(
    // Advent of Code puzzles are published on midnight EST
    ZoneId.of("America/New_York")
)

fun initDay(year: Int, day: Int) {

    val paddedDay = day.toString().padStart(2, '0')

    val solutionFile = file("src/year$year/day$paddedDay/Y${year}D${paddedDay}.kt")

    solutionFile.takeUnless { it.isFile }?.apply {
        parentFile.mkdirs()
        writeText(
            """
            package year$year.day$paddedDay
    
            import framework.solution
    
            fun main() = solution($year, $day) {
                partOne {
                
                } 
                
                partOneTest {
                
                }
            }
        """.trimIndent()
        )
    }

    logger.info(
        """
        ─────────────────────────────────────────────
        Year $year, Day $day
        $solutionFile
        ─────────────────────────────────────────────
    """.trimIndent()
    )
}

fun aocDateTimeForYear(year: Int): LocalDateTime = LocalDateTime.of(year, Month.DECEMBER, 1, 0, 0)

val LocalDateTime.isDayOfAoc get() = month == Month.DECEMBER && dayOfMonth in 1..25

//fun LocalDateTime.untilNextAoc(): Duration {
//    val nextAoc = aocDateTimeForYear(year)
//        .takeUnless { isAfter(it) }
//        ?: aocDateTimeForYear(year + 1)
//    return Duration.between(this, nextAoc)
//}

tasks.register("initToday") {
    group = "adventofcode"
    doLast {
        val dateTime = now().takeIf { it.isDayOfAoc } ?: run {
            logger.error("Today is not the day of Advent of Code")
            return@doLast
        }
        initDay(dateTime.year, dateTime.dayOfMonth)
    }
}

tasks.register("initTomorrow") {
    group = "adventofcode"
    doLast {
        val dateTime = now().plusDays(1).takeIf { it.isDayOfAoc } ?: run {
            logger.error("Tomorrow is not the day of Advent of Code")
            return@doLast
        }
        initDay(dateTime.year, dateTime.dayOfMonth)
    }
}

(1..25).forEach { day ->
    tasks.register("initDay$day") {
        group = "adventofcode"
        doLast {
            val year = defaultAdventOfCodeYear ?: run {
                logger.debug("Year is not specified, falling back to current year...")
                now().year
            }
            initDay(year, day)
        }
    }
}