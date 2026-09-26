package com.mikey.dictionary.dto.word;

import com.mikey.dictionary.entity.Word;

import java.util.Collections;
import java.util.List;

public record WordSearchResponse(
        String word,
        String phonetic,
        String audioUrl,
        List<WordMeaningResponse> meanings
) {
    public static WordSearchResponse from(Word entity) {
        List<WordMeaningResponse> meaningDtos = entity.getMeanings() == null
                ? Collections.emptyList()
                : entity.getMeanings().stream()
                .map(WordMeaningResponse::from)
                .toList();

        return new WordSearchResponse(
                entity.getWord(),
                entity.getPhonetic(),
                entity.getAudioUrl(),
                meaningDtos
        );
    }
}