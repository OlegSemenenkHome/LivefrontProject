package com.livefront.codechallenge.di

import com.livefront.codechallenge.data.Appearance
import com.livefront.codechallenge.data.Biography
import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.CharacterRepository
import com.livefront.codechallenge.data.Connections
import com.livefront.codechallenge.data.Images
import com.livefront.codechallenge.data.Powerstats
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
            slug = "superman",
            powerstats = Powerstats(
                intelligence = 94,
                strength = 100,
                speed = 96,
                durability = 100,
                power = 100,
                combat = 85
            ),
            appearance = Appearance(
                gender = "Male",
                race = "Kryptonian",
                height = listOf("6'3", "191 cm"),
                weight = listOf("235 lb", "107 kg"),
                eyeColor = "Blue",
                hairColor = "Black"
            ),
            biography = Biography(
                fullName = "Clark Kent",
                alterEgos = "Kal-El",
                aliases = listOf("Man of Steel", "Last Son of Krypton"),
                placeOfBirth = "Krypton",
                firstAppearance = "Action Comics #1 (1938)",
                publisher = "DC Comics",
                alignment = "Good"
            ),
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
            slug = "batman",
            powerstats = Powerstats(
                intelligence = 100,
                strength = 26,
                speed = 27,
                durability = 50,
                power = 47,
                combat = 100
            ),
            appearance = Appearance(
                gender = "Male",
                race = "Human",
                height = listOf("6'2\"", "188 cm"),
                weight = listOf("210 lb", "95 kg"),
                eyeColor = "Blue",
                hairColor = "Black"
            ),
            biography = Biography(
                fullName = "Bruce Wayne",
                alterEgos = "Dark Knight",
                aliases = listOf("Caped Crusader", "World's Greatest Detective"),
                placeOfBirth = "Gotham City",
                firstAppearance = "Detective Comics #27 (1939)",
                publisher = "DC Comics",
                alignment = "Good"
            ),
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
