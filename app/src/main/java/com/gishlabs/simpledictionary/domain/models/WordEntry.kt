package com.gishlabs.simpledictionary.domain.models

data class WordEntry(
    val word: String,
    val pronunciation: String?,
    val meanings: List<MeaningEntry>,
)

data class MeaningEntry(
    val partOfSpeech: PartOfSpeech,
    val definitions: List<DefinitionEntry>
)

data class DefinitionEntry(
    val definition: String,
    val example: String?,
    val synonyms: List<String>?,
    val antonyms: List<String>?
)
