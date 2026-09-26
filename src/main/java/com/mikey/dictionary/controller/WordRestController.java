package com.mikey.dictionary.controller;

import com.mikey.dictionary.dto.word.WordSearchResponse;
import com.mikey.dictionary.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{apiKey}/dictionary")
public class WordRestController {

    private final WordService wordService;

    public WordRestController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/search")
    public ResponseEntity<WordSearchResponse> searchWord(
            @RequestParam(value = "word") String word,
            @PathVariable("apiKey") String apiKey
    ) {
        WordSearchResponse response = wordService.searchWord(word, apiKey);
        return ResponseEntity.ok(response);
    }
}