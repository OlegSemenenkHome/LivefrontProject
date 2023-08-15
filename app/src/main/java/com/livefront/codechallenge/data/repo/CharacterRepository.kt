package com.livefront.codechallenge.data.repo

import com.livefront.codechallenge.data.Character

/**
 * Defines the contract for managing character data, including:
 * Retrieving all characters, and
 * fetching a specific character by its unique identifier
 */
interface CharacterRepository {

    suspend fun getCharacters(): Result<List<Character>>

    fun getCharacter(characterId: Long): Character?
}