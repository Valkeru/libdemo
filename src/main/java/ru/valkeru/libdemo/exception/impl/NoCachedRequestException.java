package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.InternalException;

public final class NoCachedRequestException extends InternalException {

    private static final String NO_REQUEST_CACHED = "В хранилище отсутствует кэшированный запрос";

    private NoCachedRequestException(String message) {
        super(message);
    }

    public static NoCachedRequestException noCachedRequest() {
        return new NoCachedRequestException(NO_REQUEST_CACHED);
    }
}
