package com.livefront.codechallenge

import com.livefront.codechallenge.data.Appearance
import com.livefront.codechallenge.data.Biography
import com.livefront.codechallenge.data.CharacterAPI
import com.livefront.codechallenge.data.CharacterRepositoryImp
import com.livefront.codechallenge.data.Connections
import com.livefront.codechallenge.data.Images
import com.livefront.codechallenge.data.Powerstats
import com.livefront.codechallenge.data.Work
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import com.livefront.codechallenge.data.Character
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class CharacterRepositoryImpTest {

    private val characters = listOf(
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

    // Mocking the API
    private val mockApi: CharacterAPI = mockk()

    // Under test
    private lateinit var repository: CharacterRepositoryImp

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = CharacterRepositoryImp(mockApi)
    }

    @Test
    fun `getCharacters fetches and stores characters`() = runTest {
        // Given
        val mockCharacters = characters
        coEvery { mockApi.getAllCharacters() } returns mockCharacters
        val result = repository.getCharacters().getOrNull()

        // Then
        assertEquals(mockCharacters, result)
    }

    @Test
    fun `getCharacters fails gracefully`() = runTest {
        // Given
        coEvery { mockApi.getAllCharacters() } throws Exception()
       val result = repository.getCharacters()

        // Then
        assert(result.isFailure)
    }

    @Test
    fun `getCharacter retrieves character by ID`() = runTest {
        // Given
        val mockCharacters = characters
        coEvery { mockApi.getAllCharacters() } returns mockCharacters
        repository.getCharacters()  // Ensure characters are loaded

        //Then
        val batman = repository.getCharacter(2L)
        assertEquals("Batman", batman.name)
    }
}