package com.gishlabs.simpledictionary.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gishlabs.simpledictionary.R
import com.gishlabs.simpledictionary.data.repository.DictionaryRepository
import com.gishlabs.simpledictionary.domain.models.SearchError
import com.gishlabs.simpledictionary.domain.models.SearchResult
import com.gishlabs.simpledictionary.domain.models.WordEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class SearchUiState {
    data class Idle(
        val formError: Int? = null
    ) : SearchUiState()
    data object Loading : SearchUiState()
    data class Error(val messageRes: Int) : SearchUiState()
    data class Success(val result: WordEntry) : SearchUiState() // This will display in a different screen, should this be a state?
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState.Idle())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun search(word: String) {
        if (word.isBlank()) {
            _uiState.update { SearchUiState.Idle(formError = R.string.search_screen_form_error_invalid_search) }
            return
        }

        Timber.d("searching: $word")
        _uiState.update { SearchUiState.Loading }

        viewModelScope.launch {
            when(val result = dictionaryRepository.search(word)) {
                is SearchResult.Error -> {
                    _uiState.update { SearchUiState.Error(
                        messageRes = getErrorMessage(result.error)
                    ) }
                }
                is SearchResult.Success -> {
                    Timber.d("${result.wordEntry}")
                    _uiState.update { SearchUiState.Success(result.wordEntry) }
                }
            }
        }
    }

    private fun getErrorMessage(error: SearchError): Int {
        return when (error) {
            SearchError.NoInternet -> R.string.error_no_internet
            SearchError.NotFound -> R.string.error_word_not_found
            SearchError.UnexpectedError -> R.string.error_unexpected
        }
    }
}
