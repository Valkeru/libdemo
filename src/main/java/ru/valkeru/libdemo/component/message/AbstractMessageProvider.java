package ru.valkeru.libdemo.component.message;

import lombok.RequiredArgsConstructor;
import ru.valkeru.libdemo.config.system.SystemConfiguration;

@RequiredArgsConstructor
public abstract class AbstractMessageProvider implements MessageProvider {

    protected static final String BAD_REQUEST_MESSAGE = "Неверный запрос";
    protected static final String INTERNAL_ERROR_MESSAGE = "Произошла внутренняя ошибка";
    protected static final String DATA_INTEGRITY_MESSAGE = "Некорректные данные. Проверьте заполнение формы";

    private final SystemConfiguration configuration;

    protected boolean isDebug() {
        return configuration.isDebugMode();
    }
}
