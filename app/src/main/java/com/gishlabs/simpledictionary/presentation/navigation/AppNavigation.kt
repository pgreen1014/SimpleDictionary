package com.gishlabs.simpledictionary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.gishlabs.simpledictionary.presentation.ui.search.SearchScreen

sealed interface Dest {
    data object Search : Dest
}

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<Dest>(Dest.Search) }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                Dest.Search -> NavEntry(key) {
                    SearchScreen()
                }
            }
        }
    )

}