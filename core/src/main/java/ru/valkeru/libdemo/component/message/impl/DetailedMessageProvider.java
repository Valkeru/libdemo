package ru.valkeru.libdemo.component.message.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.component.message.AbstractMessageProvider;
import ru.valkeru.libdemo.config.system.SystemConfiguration;
import ru.valkeru.libdemo.constants.Profiles;

@Component
@Profile({Profiles.PROFILE_DEV, Profiles.PROFILE_PRE_PRODUCTION, Profiles.PROFILE_TEST})
public class DetailedMessageProvider extends AbstractMessageProvider {

    public DetailedMessageProvider(SystemConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String getBadRequestMessage(Exception e) {
        return isDebug() ? e.getMessage() : BAD_REQUEST_MESSAGE;
    }

    @Override
    public String getInternalErrorMessage(Exception e) {
        return isDebug() ? e.getMessage() : INTERNAL_ERROR_MESSAGE;
    }

    @Override
    public String getDataIntegrityMessage(Exception e) {
        return isDebug() ? e.getMessage() : DATA_INTEGRITY_MESSAGE;
    }
}
