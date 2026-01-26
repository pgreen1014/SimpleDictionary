package com.gishlabs.simpledictionary.data.remote.dictionary_service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryService {

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/"
    }

    @GET("entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): Response<List<Word>>

}