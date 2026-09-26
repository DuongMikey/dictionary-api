package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.word.WordSearchResponse;
import com.mikey.dictionary.entity.Word;
import com.mikey.dictionary.exception.BadCredentialsException;
import com.mikey.dictionary.exception.ResourceNotFoundException;
import com.mikey.dictionary.repository.ApiKeyRepository;
import com.mikey.dictionary.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final ApiKeyRepository apiKeyRepository;

    public WordServiceImpl(WordRepository wordRepository, ApiKeyRepository apiKeyRepository) {
        this.wordRepository = wordRepository;
        this.apiKeyRepository = apiKeyRepository;
    }
    @Override
    @Transactional(readOnly = true)
    public WordSearchResponse searchWord(String word, String apiKey){
        if (!apiKeyRepository.existsByApiKeyAndIsActiveTrue(apiKey)) throw new BadCredentialsException("Invalid or inactive API key");
        Word searchWord = wordRepository.searchWord(word).orElseThrow(() -> new ResourceNotFoundException("cant find word: " + word));
        return WordSearchResponse.from(searchWord);
    }
}
