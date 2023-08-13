package com.livefront.codechallenge.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
 *  Repo to make calls, and get get a character. We store the results here,
 *  but we could also store them in a room database,
 *  but that seemed like overkill for this project
 */
internal class CharacterRepositoryImp @Inject constructor(
    private val api: CharacterAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CharacterRepository {
    private var characterList = emptyList<Character>()

    override suspend fun getCharacters(): Result<List<Character>> {
        return withContext(dispatcher) {
            runCatching {
                if (characterList.isEmpty()) {
                    characterList = api.getAllCharacters()
                }
                characterList
            }
        }
    }

    /*
     * return the character with the Id or just the
     * spot in the list if we are getting a random character
     */
    override fun getCharacter(characterId: Long): Character {
        return characterList.find { it.id == characterId } ?: characterList[characterId.toInt()]
    }
}
