package com.livefront.codechallenge.presentation.detailscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {

    private val detailKey: String = checkNotNull(savedStateHandle["detailKey"])

    var character: Character? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            character = repository.getCharacter(detailKey.toLong())
        }
    }
}