package com.mikey.dictionary.dto.api;

import java.time.LocalDateTime;

public record ApiKeyResponse(
        Integer id,
        String key,
        LocalDateTime createdAt,
        Boolean isActive
) {
}
