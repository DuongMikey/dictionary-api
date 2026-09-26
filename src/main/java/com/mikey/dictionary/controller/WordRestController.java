package com.mikey.dictionary.controller;

import com.mikey.dictionary.dto.word.WordSearchResponse;
import com.mikey.dictionary.repository.WordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/dictionary")
public class WordRestController {

    private final WordRepository repository;

    public WordRestController(WordRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/search")
    public ResponseEntity<WordSearchResponse> searchWord(
            @RequestParam(value = "word", required = true) String word
    ) {
        WordSearchResponse response = repository.searchWord(word.trim())
                .map(WordSearchResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cannot find word: " + word
                ));

        return ResponseEntity.ok(response);
    }
}