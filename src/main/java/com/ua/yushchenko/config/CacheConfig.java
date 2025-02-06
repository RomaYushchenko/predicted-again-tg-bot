package com.ua.yushchenko.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for caching.
 * Enables caching and configures cache manager.
 *
 * @author AI
 * @version 0.1-beta
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creates and configures the cache manager.
     * Uses ConcurrentMapCacheManager for in-memory caching.
     *
     * @return configured cache manager
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("predictions");
    }
} 