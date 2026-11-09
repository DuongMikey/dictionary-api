package com.mikey.dictionary.dto.word;

import java.util.List;

public record WordSearchRequest(
        List<String> words,
        Boolean skipOnError
) {}