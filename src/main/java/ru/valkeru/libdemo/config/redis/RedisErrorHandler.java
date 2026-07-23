package ru.valkeru.libdemo.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.jspecify.annotations.NonNull;

/**
 * Redis cache errors handler.
 * Exception would be logged and wrapped method will be executed.
 * Without this handler exception will be thrown up the stack, so app would be broken if cache is unavailable
 */
@Slf4j
public class RedisErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(@NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.error("Failed to read data from the cache {} by the key {}, falling back", cache.getName(), key, exception);
    }

    @Override
    public void handleCachePutError(@NonNull RuntimeException exception, @NonNull Cache cache,
                                    @NonNull Object key, Object value) {
        log.error("Failed to put data into the cache {}, key {}", cache.getName(), key, exception);
    }

    @Override
    public void handleCacheEvictError(@NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.error("Failed to evicting data from the cache {} by the key {}", cache.getName(), key, exception);
    }

    @Override
    public void handleCacheClearError(@NonNull RuntimeException exception, @NonNull Cache cache) {
        log.error("Failed to clear the cache {}", cache.getName(), exception);
    }
}
