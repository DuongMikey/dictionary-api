package com.mikey.dictionary.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {
    @Bean
    public CaffeineCacheManager wordsCaffeineCacheManager(){
        CaffeineCacheManager manager = new CaffeineCacheManager("words");
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10000)
        );
        return manager;
    }
    @Bean
    public CaffeineCacheManager apiKeysCaffeineCacheManager(){
        CaffeineCacheManager manager = new CaffeineCacheManager("api-keys");
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10)
                .expireAfterAccess(30, TimeUnit.MINUTES)
        );
        return manager;
    }


    @Bean
    @Primary
    public CacheManager cacheManager(CacheManager wordsCaffeineCacheManager, CacheManager apiKeysCaffeineCacheManager){
        CompositeCacheManager composite = new CompositeCacheManager(wordsCaffeineCacheManager, apiKeysCaffeineCacheManager);
        return composite;
    }
}
