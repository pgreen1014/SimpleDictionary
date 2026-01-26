package com.gishlabs.simpledictionary.di

import com.gishlabs.simpledictionary.data.remote.dictionary_service.DictionaryService
import com.gishlabs.simpledictionary.data.repository.DictionaryRepository
import com.gishlabs.simpledictionary.data.repository.DictionaryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DictionaryRepositoryModule {

    @Binds
    abstract fun bindDictionaryRepository(
        dictionaryRepositoryImpl: DictionaryRepositoryImpl
    ): DictionaryRepository

}