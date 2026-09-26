package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.word.WordSearchResponse;

public interface WordService {
    WordSearchResponse searchWord(String word, String apiKey);
}
