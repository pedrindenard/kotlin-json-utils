package modules

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Channel
import java.io.File

fun readAndFilterJson(inputFilePath: String, outputFilePath: String) {
    try {
        val inputFile = File(inputFilePath)
        val outputFile = File(outputFilePath)

        if (!inputFile.exists()) {
            throw IllegalArgumentException("Input file does not exist.")
        }

        val jsonString = inputFile.readText()
        val channels = Json.decodeFromString<List<Channel>>(jsonString)

        val filteredChannels = channels.filter { channel ->
            channel.logo_path != null || channel.backdrop_path != null || channel.poster_path != null
        }

        val jsonContent = Json.encodeToString(filteredChannels)
        outputFile.writeText(jsonContent)

        println("File processed successfully and saved to: ${outputFile.absolutePath}")
    } catch (e: Exception) {
        println("Error processing file: ${e.message}")
    }
}

fun main() {
    val inputDirectory = System.getProperty("user.home") + "/Downloads/Json"
    val outputDirectory = System.getProperty("user.home") + "/Downloads/Json"

    val inputFilePath = "${inputDirectory}/collections.json"
    val outputFilePath = "${outputDirectory}/collections.json"

    readAndFilterJson(inputFilePath, outputFilePath)
}