package ru.valkeru.libdemo.exception;

public abstract class RestrictedException extends RuntimeException {

    protected RestrictedException(String message) {
        super(message);
    }
}
