package modules

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Channel
import java.io.File

fun readAndSeparateJson(inputFilePath: String, outputDirectory: String, divisions: Int) {
    try {
        val inputFile = File(inputFilePath)

        if (!inputFile.exists()) {
            throw IllegalArgumentException("Input file does not exist.")
        }

        val jsonString = inputFile.readText()
        val channels = Json.decodeFromString<List<Channel>>(jsonString)

        if (divisions <= 0) {
            throw IllegalArgumentException("Divisions must be greater than 0.")
        }

        val chunkSize = (channels.size + divisions - 1) / divisions

        for (i in 0 until divisions) {
            val start = i * chunkSize
            val end = minOf(start + chunkSize, channels.size)

            if (start >= end) break

            val filename = inputFile.name.substringBefore(delimiter = ".")

            val part = channels.subList(start, end)
            val partFile = File(outputDirectory, "${filename}_part${i + 1}.json")
            partFile.writeText(Json.encodeToString(part))
        }

        println("File processed successfully and saved to: $outputDirectory")
    } catch (e: Exception) {
        println("Error processing file: ${e.message}")
    }
}

fun main() {
    val inputDirectory = System.getProperty("user.home") + "/Downloads/Json"
    val outputDirectory = System.getProperty("user.home") + "/Downloads/Json"

    val inputFilePath = "${inputDirectory}/keywords.json"

    readAndSeparateJson(inputFilePath, outputDirectory, divisions = 8)
}