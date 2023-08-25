package com.livefront.codechallenge

import android.util.Log
import com.livefront.codechallenge.data.repo.CharacterRepository
import com.livefront.codechallenge.presentation.homescreen.HomeScreenViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

internal class HomeScreenViewModelTest {

    private val mockRepo: CharacterRepository = mockk()

    // Under test
    private lateinit var viewModel: HomeScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any<String>(), any()) } returns 0
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `We should fetch characters on initialization and be in the correct state`() = runTest {
        // Given
        coEvery { mockRepo.getCharacters() } returns Result.success(characters)
        viewModel = HomeScreenViewModel(mockRepo)
        val result = viewModel.uiState.value.list

        // Then
        assertEquals(characters, result)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `The error state will be true if we are unable to fetch characters`() = runTest {
        // Given
        coEvery { mockRepo.getCharacters() } returns Result.failure(Throwable())
        viewModel = HomeScreenViewModel(mockRepo)

        // Then
        assertTrue(viewModel.uiState.value.hasError)
    }

    @Test
    fun `We should filter the list`() = runTest {
        coEvery { mockRepo.getCharacters() } returns Result.success(characters)
        viewModel = HomeScreenViewModel(mockRepo)
        viewModel.onSearchQueryChanged("Batman")

        Thread.sleep(100L) // Wait while the list filters tests have passed using 1L, but I used 100 to be safe

        //
        assertEquals(viewModel.uiState.value.filteredCharacters[0].name, "Batman")
        assertEquals(viewModel.uiState.value.filteredCharacters.size, 1)
    }

    @Test
    fun `We should be in an error state if we are unable to load the data `() = runTest {
        // Given
        coEvery { mockRepo.getCharacters() } returns Result.failure(Exception())
        viewModel = HomeScreenViewModel(mockRepo)

        assertTrue(viewModel.uiState.value.hasError)
    }

    @Test
    fun `We should be able to retry getting the characters if we are in a error state`() = runTest {
        // Given
        coEvery { mockRepo.getCharacters() } returns Result.failure(Exception())
        viewModel = HomeScreenViewModel(mockRepo)

        //Assert we are in an error state
        assertTrue(viewModel.uiState.value.hasError)

        coEvery { mockRepo.getCharacters() } returns Result.success(characters)

        //Load the new data
        viewModel.retryLoading()
        val result = viewModel.uiState.value.list

        // Then
        assertEquals(characters, result)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `Need to make sure the query state is cleared`() = runTest {
        // Given
        coEvery { mockRepo.getCharacters() } returns Result.success(characters)
        viewModel = HomeScreenViewModel(mockRepo)
        viewModel.onSearchQueryChanged("Batman")

        Thread.sleep(100L)

        //Assert we have values
        assertTrue(viewModel.uiState.value.filteredCharacters.isNotEmpty())

        //reset the state
        viewModel.clearQuery()

        //Assert the values are empty
        assertTrue(viewModel.uiState.value.filteredCharacters.isEmpty())
        assertTrue(viewModel.searchQuery.value.isEmpty())
    }

    @Test
    fun `Should be able to search for a character`() = runTest {
        // Given
        coEvery { mockRepo.getCharacters() } returns Result.success(characters)
        viewModel = HomeScreenViewModel(mockRepo)
        viewModel.searchForCharacter("Batman")

        Thread.sleep(100L)

        //Assert we have value
        assertTrue(viewModel.uiState.value.character?.name == "Batman")
    }
}