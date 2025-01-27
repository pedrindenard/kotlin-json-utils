package modules

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Channel
import java.io.File

fun readAndSortJson(inputFilePath: String, outputFilePath: String) {
    try {
        val inputFile = File(inputFilePath)
        val outputFile = File(outputFilePath)

        if (!inputFile.exists()) {
            throw IllegalArgumentException("Input file does not exist.")
        }

        val jsonString = inputFile.readText()
        val channels = Json.decodeFromString<List<Channel>>(jsonString)

        val sortedChannels = channels.sortedWith(
            compareByDescending<Channel> { channel ->
                channel.logo_path != null || channel.backdrop_path != null || channel.poster_path != null
            }.thenBy { channel ->
                channel.id
            }
        )

        val jsonContent = Json.encodeToString(sortedChannels)
        outputFile.writeText(jsonContent)

        println("File processed successfully and saved to: ${outputFile.absolutePath}")
    } catch (e: Exception) {
        println("Error processing file: ${e.message}")
    }
}

fun main() {
    val inputDirectory = System.getProperty("user.home") + "/Downloads/Json/Saved"
    val outputDirectory = System.getProperty("user.home") + "/Downloads/Json"

    val inputFilePath = "${inputDirectory}/keywords.json"
    val outputFilePath = "${outputDirectory}/keywords.json"

    readAndSortJson(inputFilePath, outputFilePath)
}