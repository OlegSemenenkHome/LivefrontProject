package com.livefront.codechallenge

import android.util.Log
import com.livefront.codechallenge.data.repo.CharacterRepositoryImpl
import com.livefront.codechallenge.presentation.homescreen.HomeScreenViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

internal class HomeScreenViewModelTest {

    private val mockRepo: CharacterRepositoryImpl = mockk()

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
    fun `getCharacters fetches the characters and the UI state is updated`() = runTest {
        // Given
        coEvery { mockRepo.getCharacters() } returns Result.success(characters)
        viewModel = HomeScreenViewModel(mockRepo)
        val result = viewModel.uiState.value.list

        // Then
        assertEquals(characters, result)
        assertEquals(viewModel.uiState.value.isLoading, false)
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

       assertEquals(viewModel.uiState.value.filteredCharacters.size, 1)
    }
}