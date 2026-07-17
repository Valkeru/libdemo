package ru.valkeru.libdemo.component.message.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.component.message.MessageProvider;

@Slf4j
@Component
@ConditionalOnMissingBean(DetailedMessageProvider.class)
public class ShortMessageProvider implements MessageProvider {

    protected static final String INTERNAL_ERROR_MESSAGE = "Internal server error";
    protected static final String DATA_INTEGRITY_MESSAGE = "Invalid data. Please check data you entered in the form";

    @PostConstruct
    protected void logComponentStarted() {
        log.info("Short error message provider started");
    }

    @Override
    public String getInternalErrorMessage(Exception ignored) {
        return INTERNAL_ERROR_MESSAGE;
    }

    @Override
    public String getDataIntegrityMessage(Exception ignored) {
        return DATA_INTEGRITY_MESSAGE;
    }
}
