package ru.valkeru.libdemo.utility;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisUtility {

    private final RedisConnectionFactory connectionFactory;

    public RedisUtility(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void clearCaches() {
        connectionFactory.getConnection().serverCommands().flushAll();
    }
}
