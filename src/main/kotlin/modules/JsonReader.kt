package modules

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import models.Channel
import models.Json.jsonDecoder
import utils.Env.apiKey
import java.io.File

suspend fun fetchChannelImages(channelType: String, inputFilePath: String, outputFilePath: String) {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(jsonDecoder)
        }
    }

    try {
        val inputFile = File(inputFilePath)

        if (!inputFile.exists()) {
            throw IllegalArgumentException("Input file does not exist.")
        }

        val jsonString = inputFile.readText()
        val networks = jsonDecoder.decodeFromString<List<Channel>>(jsonString)

        val semaphore = Semaphore(50)
        val updatedNetworks = mutableListOf<Channel>()

        coroutineScope {
            networks.forEach { channel ->
                launch(Dispatchers.IO) {
                    semaphore.withPermit {
                        try {
                            delay(1000)

                            if (
                                channel.logo_path == null &&
                                channel.backdrop_path == null &&
                                channel.poster_path == null
                            ) {
                                val response = client.get("https://api.themoviedb.org/3/$channelType/${channel.id}") {
                                    parameter("api_key", apiKey)
                                }.body<JsonObject>()

                                val logoPath = response["logo_path"]?.jsonPrimitive?.contentOrNull
                                val backdropPath = response["backdrop_path"]?.jsonPrimitive?.contentOrNull
                                val posterPath = response["poster_path"]?.jsonPrimitive?.contentOrNull

                                if (logoPath != null || backdropPath != null || posterPath != null) {
                                    println("Success fetching image for ${channel.name}, id ${channel.id}")

                                    channel.logo_path = logoPath
                                    channel.backdrop_path = backdropPath
                                    channel.poster_path = posterPath
                                } else {
                                    println("Failed to fetch image for ${channel.name}, id ${channel.id}")
                                }
                            }

                            synchronized(updatedNetworks) {
                                updatedNetworks.add(channel)
                            }
                        } catch (e: Exception) {
                            println("Error fetching data for channel with id ${channel.id}: ${e.message}")
                        }
                    }
                }
            }
        }

        val outputJson = jsonDecoder.encodeToString(updatedNetworks)
        File(outputFilePath).writeText(outputJson)

        println("JSON file successfully generated in: $outputFilePath")
    } catch (e: Exception) {
        println("Error processing file: ${e.message}")
    } finally {
        client.close()
    }
}

fun main() {
    runBlocking {
        val downloadDirectory = System.getProperty("user.home") + "/Downloads/Json"

        val inputFilePath = "$downloadDirectory/collections.json"
        val outputFilePath = "$downloadDirectory/collections_logos.json"

        fetchChannelImages(channelType = "collection", inputFilePath, outputFilePath)
    }
}