package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.api.ApiKeyResponse;
import com.mikey.dictionary.entity.ApiKey;
import com.mikey.dictionary.exception.ResourceNotFoundException;
import com.mikey.dictionary.repository.ApiKeyRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {
    private final ApiKeyRepository apiKeyRepository;

    public ApiServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    @Transactional
    public ApiKeyResponse generateKey(){
        ApiKey key = new ApiKey();
        key = apiKeyRepository.save(key);
        return new ApiKeyResponse(key.getId(), key.getApiKey(), key.getCreatedAt(),key.getIsActive());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiKeyResponse> getKeys(){
        return apiKeyRepository.getApiKeys();
    }

    @Transactional
    @Override
    @CacheEvict(value = "api-keys",key = "#result.key()")
    public ApiKeyResponse deactivateKey(Integer id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found with id: " + id));
        apiKey.setIsActive(false);
        return new ApiKeyResponse(apiKey.getId(), apiKey.getApiKey(), apiKey.getCreatedAt(), apiKey.getIsActive());
    }
    @Cacheable(value = "api-keys", key = "#apiKey", unless = "#result == false")
    @Override
    @Transactional(readOnly = true)
    public boolean isApiKeyValid(String apiKey){
        return apiKeyRepository.existsByApiKeyAndIsActiveTrue(apiKey);
    }
}
