package com.mikey.dictionary.controller;

import com.mikey.dictionary.dto.api.ApiKeyResponse;
import com.mikey.dictionary.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v3/api/admin")
public class AuthController {
    private final ApiService apiService;

    public AuthController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/keys")
    public ResponseEntity<ApiKeyResponse> generateKey(){
        ApiKeyResponse response = apiService.generateKey();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/keys/{id}")
    public ResponseEntity<Void> deactivateKey(@PathVariable Integer id) {
        apiService.deactivateKey(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/keys")
    public ResponseEntity<List<ApiKeyResponse>> getKeys(){
        List<ApiKeyResponse> responses = apiService.getKeys();
        return ResponseEntity.ok(responses);
    }
}
