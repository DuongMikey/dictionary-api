package com.mikey.dictionary.repository;

import com.mikey.dictionary.dto.api.ApiKeyResponse;
import com.mikey.dictionary.entity.ApiKey;
import com.mikey.dictionary.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiKeyRepository extends JpaRepository<ApiKey,Integer> {

    @Query("select new com.mikey.dictionary.dto.api.ApiKeyResponse(ak.id,ak.apiKey,ak.createdAt,ak.isActive) from ApiKey ak")
    List<ApiKeyResponse> getApiKeys();

    Boolean existsByApiKeyAndIsActiveTrue(String apiKey);

}
