package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.api.ApiKeyResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ApiService {
    ApiKeyResponse generateKey();

    List<ApiKeyResponse> getKeys();

    ApiKeyResponse deactivateKey(Integer id);


    boolean isApiKeyValid(String apiKey);
}
