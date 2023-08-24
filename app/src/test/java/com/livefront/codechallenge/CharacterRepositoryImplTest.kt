package com.livefront.codechallenge

import com.livefront.codechallenge.data.CharacterAPI
import com.livefront.codechallenge.data.repo.CharacterRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
internal class CharacterRepositoryImplTest {

    // Mocking the API
    private val mockApi: CharacterAPI = mockk()

    // Under test
    private lateinit var repository: CharacterRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = CharacterRepositoryImpl(mockApi)
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
        val batmanResult = repository.getCharacter(2L)
        assertTrue(batmanResult.isSuccess)
    }

    @Test
    fun `getCharacter retrieves character by ID even if cache is empty`() = runTest {
        // Given
        val mockCharacters = characters
        coEvery { mockApi.getAllCharacters() } returns emptyList()
        repository.getCharacters()  // Ensure characters are loaded
        coEvery { mockApi.getAllCharacters() } returns mockCharacters

        //Then
        val batmanResult = repository.getCharacter(2L)
        assertTrue(batmanResult.isSuccess)
    }
}