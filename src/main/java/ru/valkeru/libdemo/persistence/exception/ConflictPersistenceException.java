package ru.valkeru.libdemo.persistence.exception;

import java.io.Serial;

public class ConflictPersistenceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7242594066741329303L;

    private static final String BOOK_INSTANCE_NOT_AVAILABLE = "There is no book instance available to borrow";
    private static final String LENDING_STATUS_INVALID = "Lending status is not valid for requested operation";
    private static final String CURRENT_READERS_CARD_NOT_EXISTS = "There is no current readers card for user";
    private static final String NOT_ACTIVE_READERS_CARD = "Readers card is not active";

    private ConflictPersistenceException(String message) {
        super(message);
    }

    public static ConflictPersistenceException bookInstanceNotAvailable() {
        return new ConflictPersistenceException(BOOK_INSTANCE_NOT_AVAILABLE);
    }

    public static ConflictPersistenceException notValidLendingStatus() {
        return new ConflictPersistenceException(LENDING_STATUS_INVALID);
    }

    public static ConflictPersistenceException hasNoReadersCard() {
        return new ConflictPersistenceException(CURRENT_READERS_CARD_NOT_EXISTS);
    }

    public static ConflictPersistenceException notActiveCard() {
        return new ConflictPersistenceException(NOT_ACTIVE_READERS_CARD);
    }
}
