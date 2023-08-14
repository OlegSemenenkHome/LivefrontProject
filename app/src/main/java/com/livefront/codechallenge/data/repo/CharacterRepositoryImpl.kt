package com.livefront.codechallenge.data.repo

import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.CharacterAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
 *  Repo to make calls, and get get a character. We store the results here,
 *  but we could also store them in a room database, but that seemed
 *  unnecessary for this project
 */
internal class CharacterRepositoryImpl @Inject constructor(
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
     * return the character with the Id
     */
    override fun getCharacter(characterId: Long): Character? =
         characterList.find { it.id == characterId }
}
