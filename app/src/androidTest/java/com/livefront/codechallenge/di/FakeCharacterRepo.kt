package com.livefront.codechallenge.di

import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.repo.CharacterRepository
import com.livefront.codechallenge.data.Connections
import com.livefront.codechallenge.data.Images
import com.livefront.codechallenge.data.Work
import javax.inject.Singleton

@Singleton
class FakeCharacterRepo : CharacterRepository {

    override suspend fun getCharacters(): Result<List<Character>> {
        return Result.success(characterList)
    }

    override fun getCharacter(characterId: Long): Character {
        return characterList.find { it.id == characterId } ?: characterList[characterId.toInt()]
    }

    private val characterList = listOf(
        Character(
            id = 1,
            name = "Superman",
            work = Work(
                occupation = "Reporter",
                base = "Metropolis"
            ),
            connections = Connections(
                groupAffiliation = "Justice League",
                relatives = "Lois Lane (Wife), Jor-El (Father), Lara (Mother)"
            ),
            images = Images(
                xs = "",
                sm = "",
                md = "",
                lg = ""
            )
        ),
        Character(
            id = 2,
            name = "Batman",
            work = Work(
                occupation = "Businessman",
                base = "Gotham City"
            ),
            connections = Connections(
                groupAffiliation = "Justice League",
                relatives = "Damian Wayne (Son), Dick Grayson (Adopted Son)"
            ),
            images = Images(
                xs = "",
                sm = "",
                md = "",
                lg = ""
            )
        )
    )
}
