package com.gishlabs.simpledictionary.data.remote.dictionary_service.dto

import com.gishlabs.simpledictionary.data.remote.dictionary_service.Meaning
import com.gishlabs.simpledictionary.data.remote.dictionary_service.Word
import com.gishlabs.simpledictionary.domain.models.DefinitionEntry
import com.gishlabs.simpledictionary.domain.models.MeaningEntry
import com.gishlabs.simpledictionary.domain.models.PartOfSpeech
import com.gishlabs.simpledictionary.domain.models.WordEntry
import timber.log.Timber

fun Word.toDomainWordEntry(): WordEntry {
    return WordEntry(
        word = word,
        pronunciation = getPronunciation(this),
        meanings = getMeanings(meanings),
    )
}

private fun getPronunciation(word: Word): String? {
    return word.phonetic ?: word.phonetics?.firstNotNullOfOrNull { it.text }
}

private fun getMeanings(meanings: List<Meaning>): List<MeaningEntry> {
    return meanings.mapNotNull {
        if (it.partOfSpeech != null && it.definitions != null) {
            try {
                MeaningEntry(
                    partOfSpeech = convertPartOfSpeech(it.partOfSpeech),
                    definitions = it.definitions.mapNotNull { definition ->
                        if (definition.definition == null) return@mapNotNull null

                        DefinitionEntry(
                            definition = definition.definition,
                            example = definition.example,
                            synonyms = definition.synonyms,
                            antonyms = definition.antonyms
                        )
                    }
                )
            } catch (e: Exception) {
                Timber.e(e)
                null
            }
        } else null
    }
}

private fun convertPartOfSpeech(partOfSpeech: String): PartOfSpeech {
    return when (partOfSpeech.lowercase()) {
        "noun" -> PartOfSpeech.NOUN
        "pronoun" -> PartOfSpeech.PRONOUN
        "verb" -> PartOfSpeech.VERB
        "adjective" -> PartOfSpeech.ADJECTIVE
        "adverb" -> PartOfSpeech.ADVERB
        "preposition" -> PartOfSpeech.PREPOSITION
        "conjunction" -> PartOfSpeech.CONJUNCTION
        "interjection" -> PartOfSpeech.INTERJECTION
        "exclamation" -> PartOfSpeech.INTERJECTION
        else -> throw IllegalArgumentException("Unknown part of speech: $partOfSpeech")
    }
}