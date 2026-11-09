package com.mikey.dictionary.controller;

import com.mikey.dictionary.dto.word.WordSearchRequest;
import com.mikey.dictionary.dto.word.WordSearchResponse;
import com.mikey.dictionary.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v3/api/dictionaries")
public class WordRestController {

    private final WordService wordService;

    public WordRestController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/search")
    public ResponseEntity<WordSearchResponse> searchWord(
            @RequestParam(value = "word") String word
    ) {
        WordSearchResponse response = wordService.searchWord(word);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/search/batch")
    public ResponseEntity<List<WordSearchResponse>> searchWords(
            @RequestBody WordSearchRequest request
    ) {
        List<WordSearchResponse> response = wordService.searchWords(request);
        return ResponseEntity.ok(response);
    }

}