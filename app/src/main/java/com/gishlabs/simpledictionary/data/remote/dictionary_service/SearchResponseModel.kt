package com.gishlabs.simpledictionary.data.remote.dictionary_service

data class Word(
    val word: String,
    val phonetic: String?,
    val phonetics: List<Phonetic>?,
    val origin: String?,
    val meanings: List<Meaning>
)

data class Phonetic(
    val text: String?,
    val audio: String?
)

data class Meaning(
    val partOfSpeech: String?,
    val definitions: List<Definition>?
)

data class Definition(
    val definition: String?,
    val example: String?,
    val synonyms: List<String>?,
    val antonyms: List<String>?
)