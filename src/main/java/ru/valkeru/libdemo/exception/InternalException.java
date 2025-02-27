package ru.valkeru.libdemo.exception;

public abstract class InternalException extends RuntimeException {

    protected InternalException(String message) {
        super(message);
    }

    protected InternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
