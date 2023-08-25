package com.livefront.codechallenge.viewModels

import androidx.lifecycle.SavedStateHandle
import com.livefront.codechallenge.characters
import com.livefront.codechallenge.data.repo.CharacterRepository
import com.livefront.codechallenge.presentation.detailscreen.CharacterDetailState
import com.livefront.codechallenge.presentation.detailscreen.CharacterDetailViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

internal class CharacterDetailViewModelTest {

    private val mockRepo: CharacterRepository = mockk()
    private val mockkSaveStateHandle: SavedStateHandle = mockk()

    // Under test
    private lateinit var viewModel: CharacterDetailViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `We should be able to fetch the character when the ViewModel is initialized`() = runTest {
        // Given
        coEvery { mockRepo.getCharacter(2) } returns Result.success(characters[1])
        every { mockkSaveStateHandle.get<String>(any()) } returns "2"
        viewModel =
            CharacterDetailViewModel(savedStateHandle = mockkSaveStateHandle, repository = mockRepo)
        val result = viewModel.uiState.value
        // Then
        assert(result is CharacterDetailState.Success)
    }

    @Test
    fun `We should be able to retry getting the character when the ViewModel didn't have it previously`() = runTest {
        // Given
        coEvery { mockRepo.getCharacter(2) } returns Result.failure(Exception())
        every { mockkSaveStateHandle.get<String>(any()) } returns "2"
        viewModel = CharacterDetailViewModel(savedStateHandle = mockkSaveStateHandle, repository = mockRepo)

        //Assert the state is error
        assert(viewModel.uiState.value is CharacterDetailState.Error)
        coEvery { mockRepo.getCharacter(2) } returns Result.success(characters[1])
        viewModel.retryLoading()

        // Then
        assert(viewModel.uiState.value is CharacterDetailState.Success)
    }
}