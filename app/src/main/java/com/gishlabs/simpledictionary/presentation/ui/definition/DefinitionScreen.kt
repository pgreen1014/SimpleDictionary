package com.gishlabs.simpledictionary.presentation.ui.definition

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gishlabs.simpledictionary.domain.models.WordEntry
import com.gishlabs.simpledictionary.presentation.theme.SimpleDictionaryTheme

@Composable
fun DefinitionScreen(
    word: WordEntry,
    modifier: Modifier = Modifier
) {
    Scaffold() { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(word.word)
        }
    }
}

@Preview
@Composable
fun DefinitionScreenPreview() {
    SimpleDictionaryTheme {
        DefinitionScreen(
            word = WordEntry(
                word = "Hello",
                pronunciation = null,
                meanings = emptyList()
            )
        )
    }
}