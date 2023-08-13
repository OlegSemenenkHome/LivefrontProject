package com.livefront.codechallenge.data

import retrofit2.http.GET

interface CharacterAPI {
    @GET("all.json")
    suspend fun getAllCharacters(): List<Character>
}
