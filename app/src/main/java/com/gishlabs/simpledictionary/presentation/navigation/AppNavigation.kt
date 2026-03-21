package com.gishlabs.simpledictionary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.gishlabs.simpledictionary.domain.models.WordEntry
import com.gishlabs.simpledictionary.presentation.ui.definition.DefinitionScreen
import com.gishlabs.simpledictionary.presentation.ui.search.SearchScreen
import timber.log.Timber

sealed interface Dest {
    data object Search : Dest
    data class Definition(val word: WordEntry) : Dest
}

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<Dest>(Dest.Search) }
    Timber.d("backstack size - ${backStack.size}")
    NavDisplay(
        backStack = backStack,
        onBack = {
            Timber.d("onBack")
            backStack.removeLastOrNull()
            Timber.d("\tbackstack size - ${backStack.size}")
        },
        entryProvider = { key ->
            when (key) {
                is Dest.Search -> NavEntry(key) {
                    SearchScreen(
                        onSearchSuccess = {
                            Timber.d("On search Success")
                            backStack.add(Dest.Definition(it))
                            Timber.d("\tbackstack size - ${backStack.size}")
                        }
                    )
                }
                is Dest.Definition -> NavEntry(key) {
                    DefinitionScreen(key.word)
                }
            }
        }
    )

}