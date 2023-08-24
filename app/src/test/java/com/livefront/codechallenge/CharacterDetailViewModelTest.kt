package com.livefront.codechallenge

import androidx.lifecycle.SavedStateHandle
import com.livefront.codechallenge.data.repo.CharacterRepositoryImpl
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

    private val mockRepo: CharacterRepositoryImpl = mockk()
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
}