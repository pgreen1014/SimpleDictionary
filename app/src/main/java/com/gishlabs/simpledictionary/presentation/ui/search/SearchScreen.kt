package com.gishlabs.simpledictionary.presentation.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gishlabs.simpledictionary.R
import com.gishlabs.simpledictionary.presentation.theme.SimpleDictionaryTheme
import com.gishlabs.simpledictionary.presentation.ui.components.ErrorMessage
import timber.log.Timber

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val screenState = searchViewModel.uiState.collectAsStateWithLifecycle()
    SearchScreen(
        screenState = screenState.value,
        onLookupWord = { word -> searchViewModel.search(word) }
    )
}

@Composable
private fun SearchScreen(
    screenState: SearchUiState,
    onLookupWord: (String) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            when (screenState) {
                is SearchUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        ErrorMessage(stringResource(screenState.messageRes))
                        SearchTextField { onLookupWord(it) }
                    }
                }
                is SearchUiState.Idle -> {
                    SearchTextField(
                        formError = screenState.formError,
                        onLookupWord = { onLookupWord(it) }
                    )
                }
                SearchUiState.Loading -> {
                    Timber.d("Showing Loading")
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
                is SearchUiState.Success -> {
                    Timber.d("Search - Success")
                    SearchTextField(
                        formError = null,
                        onLookupWord = { onLookupWord(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    formError: Int? = null,
    onLookupWord: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        supportingText = formError?.let {{
            Text(
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error
            )
        }},
        isError = formError != null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        singleLine = true,
        shape = RoundedCornerShape(36.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onLookupWord(searchText) },
        )
    )
}

@Preview
@Composable
fun SearchScreenPreview() {
    SimpleDictionaryTheme {
        SearchScreen(
            screenState = SearchUiState.Idle(),
            onLookupWord = { }
        )
    }
}

@Preview
@Composable
fun SearchScreenLoadingPreview() {
    SimpleDictionaryTheme {
        SearchScreen(
            screenState = SearchUiState.Loading,
            onLookupWord = { }
        )
    }
}

@Preview
@Composable
fun SearchScreenErrorPreview() {
    SimpleDictionaryTheme {
        SearchScreen(
            screenState = SearchUiState.Error(R.string.error_unexpected),
            onLookupWord = { }
        )
    }
}