package ru.valkeru.libdemo.domain.exception;

public final class DomainNotFoundException extends RuntimeException {

    private static final String BOOK_INSTANCE_NOT_FOUND = "Book instance not found";

    private DomainNotFoundException(String message) {
        super(message);
    }

    public static DomainNotFoundException bookInstance() {
        return new DomainNotFoundException(BOOK_INSTANCE_NOT_FOUND);
    }
}
