package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.word.WordSearchRequest;
import com.mikey.dictionary.dto.word.WordSearchResponse;
import com.mikey.dictionary.entity.Word;
import com.mikey.dictionary.exception.ResourceNotFoundException;
import com.mikey.dictionary.repository.WordRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;
    private final CacheManager cacheManager;

    public WordServiceImpl(WordRepository wordRepository, CacheManager cacheManager) {
        this.wordRepository = wordRepository;
        this.cacheManager = cacheManager;
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
    @Transactional(readOnly = true)
    @Override
    public List<WordSearchResponse> searchWords(WordSearchRequest request) {
        Cache cache = cacheManager.getCache("words");
        boolean skipOnError = Boolean.TRUE.equals(request.skipOnError());

        List<String> inputWords = (request.words() == null) ? List.of() : request.words();

        List<String> normalizedWords = inputWords.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();

        if (normalizedWords.isEmpty()) {
            return List.of();
        }

        List<WordSearchResponse> result = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String word : normalizedWords) {
            WordSearchResponse response = (cache != null) ? cache.get(word, WordSearchResponse.class) : null;
            if (response != null) {
                result.add(response);
            } else {
                missing.add(word);
            }
        }

        if (missing.isEmpty()) {
            return result;
        }

        List<Word> searchWords = wordRepository.searchWords(missing);
        if (searchWords == null) {
            searchWords = List.of();
        }

        if (!skipOnError) {
            Set<String> foundInDbNames = searchWords.stream()
                    .map(w -> w.getWord().trim().toLowerCase())
                    .collect(Collectors.toSet());

            List<String> trulyMissingWords = missing.stream()
                    .filter(word -> !foundInDbNames.contains(word))
                    .toList();

            if (!trulyMissingWords.isEmpty()) {
                throw new ResourceNotFoundException("cant find words: " + trulyMissingWords);
            }
        }

        for (Word word : searchWords) {
            WordSearchResponse response = WordSearchResponse.from(word);
            result.add(response);

            if (cache != null) {
                cache.put(word.getWord().trim().toLowerCase(), response);
            }
        }

        return result;
    }
}
