package ru.valkeru.libdemo.component.message.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.component.message.AbstractMessageProvider;
import ru.valkeru.libdemo.config.system.SystemConfiguration;
import ru.valkeru.libdemo.constants.Profiles;

@Component
@Profile(Profiles.PROFILE_PRODUCTION)
public class ShortMessageProvider extends AbstractMessageProvider {

    public ShortMessageProvider(SystemConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String getBadRequestMessage(Exception ignored) {
        return BAD_REQUEST_MESSAGE;
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
