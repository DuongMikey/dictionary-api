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
                entity.getSynonyms(),
                entity.getAntonyms(),
                entity.getDefinition(),
                entity.getExample()
        );
    }

}