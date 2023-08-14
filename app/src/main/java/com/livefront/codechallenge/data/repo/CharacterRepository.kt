package com.livefront.codechallenge.data.repo

import com.livefront.codechallenge.data.Character

/**
 * This interface for a repo is for getting and storing the Characters.
 * Also for fetch a specific character if we need it
 */
interface CharacterRepository {

    suspend fun getCharacters(): Result<List<Character>>

    fun getCharacter(characterId: Long): Character?
}