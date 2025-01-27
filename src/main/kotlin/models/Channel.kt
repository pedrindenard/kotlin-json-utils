@file:Suppress("OPT_IN_USAGE")

package models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

@Serializable
data class Channel(
    var id: Int,
    var name: String,
    var logo_path: String? = null,
    var backdrop_path: String? = null,
    var poster_path: String? = null
)