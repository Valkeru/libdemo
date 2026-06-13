package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.InternalException;

public final class NoCachedRequestException extends InternalException {

    private static final String NO_REQUEST_CACHED = "No cached request in the storage";

    private NoCachedRequestException(String message) {
        super(message);
    }

    public static NoCachedRequestException noCachedRequest() {
        return new NoCachedRequestException(NO_REQUEST_CACHED);
    }
}
