package ru.valkeru.libdemo.config.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfiguration implements CachingConfigurer {

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisErrorHandler();
    }

    @Bean("defaultRedisCacheConfiguration")
    public RedisCacheConfiguration defaultRedisCacheConfiguration(@Value("${app.system.redis-prefix}") String prefix,
                                                                  ObjectMapper mapper) {
        ObjectMapper objectMapper = mapper.rebuild()
                .activateDefaultTyping(
                        mapper.serializationConfig().getPolymorphicTypeValidator(),
                        DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY
                )
                .build();

        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("%s::".formatted(prefix))
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJacksonJsonRedisSerializer(objectMapper)
                ));
    }

    @Bean("jwtRedisConfiguration")
    public RedisCacheConfiguration jwtRedisConfiguration(@Value("${app.system.redis-prefix}") String prefix,
                                                         @Value("${app.security.jwt.lifetime}") long ttl,
                                                         ObjectMapper mapper) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("%s::jwt::".formatted(prefix))
                .entryTtl(Duration.ofSeconds(ttl))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJacksonJsonRedisSerializer(mapper)
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
