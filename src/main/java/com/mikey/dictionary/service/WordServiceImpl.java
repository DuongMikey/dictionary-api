package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.word.WordSearchResponse;
import com.mikey.dictionary.entity.Word;
import com.mikey.dictionary.exception.BadCredentialsException;
import com.mikey.dictionary.exception.ResourceNotFoundException;
import com.mikey.dictionary.repository.ApiKeyRepository;
import com.mikey.dictionary.repository.WordRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;

    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }
    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "words",
            key = "#word != null ? #word.trim().toLowerCase() : ''",
            condition = "#word != null && !#word.trim().isEmpty()"
    )
    public WordSearchResponse searchWord(String word){
        Word searchWord = wordRepository.searchWord(word.trim().toLowerCase()).orElseThrow(() -> new ResourceNotFoundException("cant find word: " + word));
        return WordSearchResponse.from(searchWord);
    }
}
