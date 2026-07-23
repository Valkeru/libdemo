package ru.valkeru.libdemo;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.valkeru.libdemo.constants.AppEnvironment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TestConfiguration(proxyBeanMethods = false)
public class ApplicationTestConfiguration {

    private static PostgreSQLContainer<?> postgreSQLContainer;
    private static RedisContainer redisContainer;

    static {
        System.setProperty(
                AppEnvironment.ENV_INSTANCE_ID,
                String.format(
                        "test-%s",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").format(LocalDateTime.now())
                )
        );
    }


    @Bean
    @ServiceConnection
    @Profile("!testcontainers-disabled")
    public PostgreSQLContainer<?> getPostgresContainer() {
        if (postgreSQLContainer == null) {
            postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1"))
                .withUrlParam("reWriteBatchedInserts", "true")
                .withCommand("-c", "max_connections=1000");
        }

        return postgreSQLContainer;
    }

    @Bean
    @ServiceConnection
    @Profile("!testcontainers-disabled")
    public RedisContainer getRedisContainer() {
        if (redisContainer == null) {
            redisContainer = new RedisContainer(DockerImageName.parse("redis:6.2.6"));
        }

        return redisContainer;
    }
}
