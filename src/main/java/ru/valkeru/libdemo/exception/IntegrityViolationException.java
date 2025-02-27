package ru.valkeru.libdemo.exception;

public abstract class IntegrityViolationException extends RuntimeException {

  protected IntegrityViolationException(String message) {
        super(message);
    }
}
