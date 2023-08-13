package com.livefront.codechallenge.data

interface CharacterRepository {

    suspend fun getCharacters(): Result<List<Character>>

    fun getCharacter(characterId: Long): Character
}