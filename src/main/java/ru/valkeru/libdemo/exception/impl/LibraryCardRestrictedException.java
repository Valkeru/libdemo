package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.RestrictedException;

public class LibraryCardRestrictedException extends RestrictedException {

    private static final String CARD_BLOCKED_MESSAGE = "Its impossible to create library card. Please contact support";

    private static final String CARD_EXISTS_MESSAGE = "Library card already exists";

    private LibraryCardRestrictedException(String message) {
        super(message);
    }

    public static LibraryCardRestrictedException blocked() {
        return new LibraryCardRestrictedException(CARD_BLOCKED_MESSAGE);
    }

    public static LibraryCardRestrictedException exists() {
        return new LibraryCardRestrictedException(CARD_EXISTS_MESSAGE);
    }
}
