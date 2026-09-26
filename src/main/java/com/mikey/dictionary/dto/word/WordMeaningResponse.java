package com.mikey.dictionary.dto.word;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mikey.dictionary.entity.WordMeaning;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WordMeaningResponse(
        String partOfSpeech,
        List<String> synonyms,
        List<String> antonyms,
        String definition,
        String example
) {
    public static WordMeaningResponse from(WordMeaning entity) {
        return new WordMeaningResponse(
                entity.getPartOfSpeech(),
                splitToList(entity.getSynonyms()),
                splitToList(entity.getAntonyms()),
                entity.getDefinition(),
                entity.getExample()
        );
    }

    private static List<String> splitToList(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}