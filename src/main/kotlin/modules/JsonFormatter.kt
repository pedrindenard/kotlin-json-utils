package modules

import java.io.File

fun formatJsonFile(inputFilePath: String, outputFilePath: String) {
    try {
        val inputFile = File(inputFilePath)
        val outputFile = File(outputFilePath)

        if (!inputFile.exists()) {
            throw IllegalArgumentException("Input file does not exist.")
        }

        val lines = inputFile.readLines()
        val updatedLines = lines.mapIndexed { index, line -> if (index == lines.lastIndex) line else "$line," }

        outputFile.writeText("[" + updatedLines.joinToString(System.lineSeparator()) + "]")

        println("File processed successfully and saved to: ${outputFile.absolutePath}")
    } catch (e: Exception) {
        println("Error processing file: ${e.message}")
    }
}

fun main() {
    val downloadDirectory = System.getProperty("user.home") + "/Downloads/Json"

    val inputFilePath = "${downloadDirectory}/file.json"
    val outputFilePath = "${downloadDirectory}/output.json"

    formatJsonFile(inputFilePath, outputFilePath)
}