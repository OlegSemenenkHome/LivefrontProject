package com.livefront.codechallenge.presentation.detailscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.repo.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)
    val uiState: StateFlow<CharacterDetailState> = _uiState

    private val detailKey: String = checkNotNull(savedStateHandle["detailKey"])

    init {
        viewModelScope.launch {
            repository.getCharacter(detailKey.toLong())
                .onSuccess {
                    _uiState.value = CharacterDetailState.Success(it)
                }
                .onFailure {
                    _uiState.value = CharacterDetailState.Error
                }
        }
    }

    fun retryLoading() {
        _uiState.value = CharacterDetailState.Loading
        viewModelScope.launch {
            repository.getCharacter(detailKey.toLong())
                .onSuccess {
                    _uiState.value = CharacterDetailState.Success(it)
                }
                .onFailure {
                    _uiState.value = CharacterDetailState.Error
                }
        }
    }
}

sealed class CharacterDetailState {
    data object Loading : CharacterDetailState()
    data object Error : CharacterDetailState()
    data class Success(val character: Character) : CharacterDetailState()
}
