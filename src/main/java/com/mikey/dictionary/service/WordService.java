package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.word.WordSearchRequest;
import com.mikey.dictionary.dto.word.WordSearchResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordService {
    WordSearchResponse searchWord(String word);

    @Transactional(readOnly = true)
    List<WordSearchResponse> searchWords(WordSearchRequest request);
}
