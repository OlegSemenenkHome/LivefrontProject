package com.livefront.codechallenge.data

import com.squareup.moshi.Json

data class Character(
    val id: Long,
    val name: String,
    val work: Work,
    val connections: Connections,
    val images: Images,
)

data class Work(
    val occupation: String,
    val base: String,
)

data class Connections(
    val groupAffiliation: String,
    val relatives: String,
)

data class Images(
    @field:Json(name = "xs") val extraSmall: String,
    @field:Json(name = "sm") val small: String,
    @field:Json(name = "md") val medium: String,
    @field:Json(name = "lg") val large: String,
)
