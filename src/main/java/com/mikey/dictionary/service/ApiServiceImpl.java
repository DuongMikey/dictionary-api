package com.mikey.dictionary.service;

import com.mikey.dictionary.dto.api.ApiKeyResponse;
import com.mikey.dictionary.entity.ApiKey;
import com.mikey.dictionary.repository.ApiKeyRepository;
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
    public void deactivateKey(Integer id){
        ApiKey apiKey = apiKeyRepository.getReferenceById(id);
        apiKey.setIsActive(false);
        apiKeyRepository.save(apiKey);
    }
}
