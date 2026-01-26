package com.gishlabs.simpledictionary.data.repository

import com.gishlabs.simpledictionary.data.remote.dictionary_service.DictionaryService
import com.gishlabs.simpledictionary.data.remote.dictionary_service.dto.toDomainWordEntry
import com.gishlabs.simpledictionary.domain.models.SearchError
import com.gishlabs.simpledictionary.domain.models.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface DictionaryRepository {

    suspend fun search(word: String): SearchResult

}

class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryService: DictionaryService
): DictionaryRepository {

    init {
        Timber.d("Initializing DictionaryRepositoryImpl")
    }


    override suspend fun search(word: String): SearchResult = withContext(Dispatchers.IO) {
        try {
            Timber.d("Searching for word: $word")
            val response = dictionaryService.getDefinition(word)
            if (response.isSuccessful) {
                Timber.d("Dictionary API query Success")
                Timber.d("\t- result ${response.body()}")
                val wordResponse = response.body()?.get(0)
                if (wordResponse != null) {
                    return@withContext SearchResult.Success(wordResponse.toDomainWordEntry())
                } else {
                    Timber.e("Response in unexpected format")
                    return@withContext SearchResult.Error(SearchError.UnexpectedError)
                }
            } else {
                Timber.e("API Error: ${response.code()} - ${response.errorBody()?.string()}")
                return@withContext SearchResult.Error(SearchError.NotFound)
            }
        } catch (e: Exception) {
            Timber.e("Network or unexpected error: ${e.message}")
            return@withContext SearchResult.Error(SearchError.NoInternet)
        }
    }

}
