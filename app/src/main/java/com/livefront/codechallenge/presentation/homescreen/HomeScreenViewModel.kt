package com.livefront.codechallenge.presentation.homescreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.repo.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val VIEWMODEL_LOGTAG = "HOMESCREEN_VIEWMODEL"

@HiltViewModel
internal class HomeScreenViewModel @Inject constructor(
    private val repo: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUIState(isLoading = true))
    val uiState: StateFlow<HomeScreenUIState> = _uiState

    val searchQuery: MutableState<String> = mutableStateOf("")

    init {
        viewModelScope.launch {
            repo.getCharacters().onSuccess {
                loadSuccess(it)
            }.onFailure {
                loadFailure(it)
                Log.e(VIEWMODEL_LOGTAG, "Unable to load characters", it)
            }

            stopLoading()
        }
    }

    fun retryLoading() {
        startLoading()

        viewModelScope.launch {
            repo.getCharacters().onSuccess {
                loadSuccess(it)
            }.onFailure {
                loadFailure(it)
            }

            stopLoading()
        }
    }

    /*
     * Method to see if the search query matches what we have in a list
     */
    fun searchForCharacter(query: String) {
        val character = uiState.value.list.find { character ->
            clearQuery()
            character.name.lowercase() == query.lowercase()
        }

        _uiState.update { state ->
            state.copy(character = character)
        }
    }

    /*
     * Updating the list with the new string
     * done off the main thread to ensure smooth UI experience
     */
    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        viewModelScope.launch {
            getFilteredCharacters(query, uiState.value.list).let { filteredList ->
                _uiState.update { state ->
                    state.copy(filteredCharacters = filteredList)
                }
            }
        }
    }

    /*
     * Clean up from using the search
     */
    fun clearQuery() {
        searchQuery.value = ""
        _uiState.update { state ->
            state.copy(filteredCharacters = emptyList())
        }
    }

    fun characterNavigated() {
        _uiState.update { state ->
            state.copy(character = null)
        }
    }

    private fun startLoading() {
        _uiState.update { state ->
            state.copy(isLoading = true, hasError = false)
        }
    }

    private fun loadSuccess(characters: List<Character>) {
        _uiState.update { state ->
            state.copy(list = characters)
        }
    }

    private fun loadFailure(e: Throwable) {
        _uiState.update { state ->
            state.copy(hasError = true)
        }
        Log.e(VIEWMODEL_LOGTAG, "Unable to load characters", e)
    }

    private fun stopLoading() {
        _uiState.update { state ->
            state.copy(isLoading = false)
        }
    }

    /*
     * Filtering the list, string must match the start of the character
     */
    private suspend fun getFilteredCharacters(
        query: String,
        listAssets: List<Character>,
    ): List<Character> {
        return withContext(Dispatchers.Default) {
            if (query.isEmpty()) emptyList<Character>()
            listAssets.filter { it.name.lowercase().startsWith(query.lowercase()) }
        }
    }
}

data class HomeScreenUIState(
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val filteredCharacters: List<Character> = emptyList(),
    val list: List<Character> = emptyList(),
    val character: Character? = null
)
