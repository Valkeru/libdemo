package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.RestrictedException;

public class ReadersCardRestrictedException extends RestrictedException {

    private static final String CARD_BLOCKED_MESSAGE = "Its impossible to create readers card. Please contact support";

    private static final String CARD_EXISTS_MESSAGE = "Readers card already exists";

    private ReadersCardRestrictedException(String message) {
        super(message);
    }

    public static ReadersCardRestrictedException blocked() {
        return new ReadersCardRestrictedException(CARD_BLOCKED_MESSAGE);
    }

    public static ReadersCardRestrictedException exists() {
        return new ReadersCardRestrictedException(CARD_EXISTS_MESSAGE);
    }
}
