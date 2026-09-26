package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.api.ApiKeyResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApiService {
    ApiKeyResponse generateKey();

    List<ApiKeyResponse> getKeys();

    @Transactional
    void deactivateKey(Integer id);
}
