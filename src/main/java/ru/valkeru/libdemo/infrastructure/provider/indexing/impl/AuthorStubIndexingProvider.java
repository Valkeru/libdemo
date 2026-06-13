package ru.valkeru.libdemo.infrastructure.provider.indexing.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.infrastructure.provider.indexing.AuthorIndexingProvider;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.system.elasticsearch-enabled", havingValue = "false", matchIfMissing = true)
public class AuthorStubIndexingProvider implements AuthorIndexingProvider {

    @PostConstruct
    protected void logComponentStarted() {
        log.info("Stub indexing provider started");
    }
}
