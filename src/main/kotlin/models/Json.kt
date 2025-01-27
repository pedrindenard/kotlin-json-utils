@file:Suppress("OPT_IN_USAGE")

package models

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

object Json {

    val jsonDecoder = Json {
        jsonIgnoreUnknownKeys()
        jsonExplicitNulls()
        jsonIsLenient()
    }

    private fun JsonBuilder.jsonIgnoreUnknownKeys() {
        ignoreUnknownKeys = true
    }

    private fun JsonBuilder.jsonExplicitNulls() {
        explicitNulls = true
    }

    private fun JsonBuilder.jsonIsLenient() {
        isLenient = true
    }

}