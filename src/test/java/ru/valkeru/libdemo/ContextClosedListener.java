package ru.valkeru.libdemo;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Component
@Profile("testcontainers-disabled")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {

    DataSource dataSource;

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();

            statement.execute("DROP SCHEMA IF EXISTS public CASCADE;");
            statement.execute("DROP SCHEMA IF EXISTS library CASCADE;");
            statement.execute("DROP SCHEMA IF EXISTS security CASCADE;");

            log.info("Test database successfully purged");
        } catch (SQLException e) {
            log.error("Database purge failed, reason: {}", e.getMessage(), e);

            throw new RuntimeException(e);
        }
    }
}
