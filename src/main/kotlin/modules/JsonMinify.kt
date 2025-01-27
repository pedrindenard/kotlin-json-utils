package modules

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Channel
import java.io.File

fun readAndMinifyJson(inputFilePath: String, outputFilePath: String) {
    try {
        val inputFile = File(inputFilePath)
        val outputFile = File(outputFilePath)

        if (!inputFile.exists()) {
            throw IllegalArgumentException("Input file does not exist.")
        }

        val jsonString = inputFile.readText()
        val channels = Json.decodeFromString<List<Channel>>(jsonString)

        val minifiedJson = Json.encodeToString(channels)
        outputFile.writeText(minifiedJson)

        println("File minified and saved to: ${outputFile.absolutePath}")
    } catch (e: Exception) {
        println("Error processing file: ${e.message}")
    }
}

fun main() {
    val inputDirectory = System.getProperty("user.home") + "/Downloads/Json"
    val outputDirectory = System.getProperty("user.home") + "/Downloads/Json"

    val inputFilePath = "${inputDirectory}/keywords.json"
    val outputFilePath = "${outputDirectory}/keywords_minify.json"

    readAndMinifyJson(inputFilePath, outputFilePath)
}