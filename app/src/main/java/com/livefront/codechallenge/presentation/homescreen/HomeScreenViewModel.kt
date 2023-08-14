package com.livefront.codechallenge.presentation.homescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefront.codechallenge.data.Character
import com.livefront.codechallenge.data.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val VIEWMODEL_LOGTAG = "HOMESCREEN_VIEWMODEL"

@HiltViewModel
internal class HomeScreenViewModel @Inject constructor(
    private val repo: CharacterRepository
) : ViewModel() {
    val characterList = mutableStateListOf<Character>()

    var loading: Boolean by mutableStateOf(true)
        private set

    var filteredCharacters by mutableStateOf(emptyList<Character>())
        private set

    var searchQuery: String by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            repo.getCharacters().onSuccess {
                characterList.addAll(it)
            }.onFailure {
                Log.e(VIEWMODEL_LOGTAG, "Unable to load characters", it)
            }
            loading = false
        }
    }

    /*
     * Method to see if the search query matches what we have in a list
     */
    fun getCharacter(query: String): Character? {
        characterList.forEach { character ->
            if (character.name.lowercase().contentEquals(query.lowercase())) return character
        }
        clearQuery()
        return null
    }

    /*
     * Updating the list with the new string
     */
    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                filteredCharacters = getFilteredCharacters(query, characterList)
            }
        }
    }

    /*
     * Clean up from using the search
     */
    fun clearQuery() {
        searchQuery = ""
        filteredCharacters = emptyList()
    }

    /*
     * Filtering the list, string must match the start of the character
     */
    private fun getFilteredCharacters(
        query: String,
        listAssets: List<Character>,
    ): List<Character> {
        if (query.isEmpty()) return emptyList()
        return listAssets.filter { it.name.lowercase().startsWith(query.lowercase()) }
    }
}