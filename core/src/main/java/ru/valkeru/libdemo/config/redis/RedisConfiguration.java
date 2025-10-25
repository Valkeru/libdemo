package ru.valkeru.libdemo.config.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfiguration implements CachingConfigurer {

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisErrorHandler();
    }

    @Bean("defaultRedisCacheConfiguration")
    public RedisCacheConfiguration defaultRedisCacheConfiguration(ObjectMapper mapper) {
        ObjectMapper objectMapper = mapper.copy()
                .activateDefaultTyping(
                        mapper.getPolymorphicTypeValidator(),
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY
                );

        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("libdemo")
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(objectMapper)
                ));
    }

    @Bean("jwtRedisConfiguration")
    public RedisCacheConfiguration jwtRedisConfiguration(@Value("${app.security.jwt.lifetime}") long ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("libdemo-jwt")
                .entryTtl(Duration.ofSeconds(ttl))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()
                ));
    }

    @Bean
    @Primary
    public CacheManager defaultCacheManager(RedisCacheConfiguration defaultRedisCacheConfiguration,
                                            RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultRedisCacheConfiguration)
                .build();
    }

    @Bean("jwtCacheManager")
    public CacheManager jwtCacheManager(RedisCacheConfiguration jwtRedisConfiguration,
                                        RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(jwtRedisConfiguration)
                .build();
    }
}
