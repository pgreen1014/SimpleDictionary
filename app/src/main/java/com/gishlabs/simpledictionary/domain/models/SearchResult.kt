package com.gishlabs.simpledictionary.domain.models

sealed class SearchResult {
    data class Success(val wordEntry: WordEntry) : SearchResult()
    data class Error(val error: SearchError) : SearchResult()
}

sealed class SearchError {
    data object NotFound : SearchError()
    data object NoInternet : SearchError()
    data object UnexpectedError : SearchError()
}