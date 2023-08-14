package com.livefront.codechallenge.data

data class Character(
    val id: Long,
    val name: String,
    val biography: Biography,
    val work: Work,
    val connections: Connections,
    val images: Images,
)

data class Biography(
    val fullName: String,
    val alterEgos: String,
    val aliases: List<String>,
    val placeOfBirth: String,
    val firstAppearance: String,
    val publisher: String?,
    val alignment: String,
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
    val xs: String,
    val sm: String,
    val md: String,
    val lg: String,
)
