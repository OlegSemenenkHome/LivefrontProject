package com.livefront.codechallenge.data.repo

import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.CharacterAPI
import javax.inject.Inject

/*
 *   We store the results here, but we could also
 * store them in a room database, but that seemed
 *  unnecessary for this project
 */
internal class CharacterRepositoryImpl @Inject constructor(
    private val api: CharacterAPI,
) : CharacterRepository {
    private var characterList = emptyList<Character>()

    override suspend fun getCharacters(): Result<List<Character>> {
        return runCatching {
            if (characterList.isEmpty()) {
                characterList = api.getAllCharacters()
            }
            characterList
        }
    }

    /*
     * If the cache is empty we make a call, then we
     * return the character with the Id
     */
    override suspend fun getCharacter(characterId: Long): Result<Character> {
        return runCatching {
            if (characterList.isEmpty()) {
                getCharacters()
            }
            characterList.find { it.id == characterId }
                ?: throw Exception("Unable to get Character")
        }
    }
}
