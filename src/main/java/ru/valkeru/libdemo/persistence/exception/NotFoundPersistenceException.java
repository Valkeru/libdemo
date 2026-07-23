package ru.valkeru.libdemo.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class NotFoundPersistenceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6448436209852518880L;

    private static final String BOOK_INSTANCE_NOT_FOUND = "Book instance not found";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String READERS_CARD_NOT_FOUND = "Readers card not found";
    private static final String LENDING_NOT_FOUND = "Lending is not found";

    private NotFoundPersistenceException(String message) {
        super(message);
    }

    public static NotFoundPersistenceException bookInstance() {
        return new NotFoundPersistenceException(BOOK_INSTANCE_NOT_FOUND);
    }

    public static NotFoundPersistenceException user() {
        return new NotFoundPersistenceException(USER_NOT_FOUND);
    }

    public static NotFoundPersistenceException readersCard() {
        return new NotFoundPersistenceException(READERS_CARD_NOT_FOUND);
    }

    public static NotFoundPersistenceException lending() {
        return new NotFoundPersistenceException(LENDING_NOT_FOUND);
    }
}
