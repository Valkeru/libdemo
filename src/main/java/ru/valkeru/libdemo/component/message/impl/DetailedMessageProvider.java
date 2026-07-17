package ru.valkeru.libdemo.component.message.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.component.message.MessageProvider;
import ru.valkeru.libdemo.constants.Profiles;

@Slf4j
@Component
@Profile({Profiles.PROFILE_DEV})
public class DetailedMessageProvider implements MessageProvider {

    @PostConstruct
    protected void logComponentStarted() {
        log.info("Detailed error message provider started");
    }

    @Override
    public String getInternalErrorMessage(Exception e) {
        return getExceptionMessage(e);
    }

    @Override
    public String getDataIntegrityMessage(Exception e) {
        return getExceptionMessage(e);
    }

    private String getExceptionMessage(Exception e) {
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        String message = rootCause.getMessage();

        return StringUtils.isNotBlank(message) ? message : e.getMessage();
    }
}
