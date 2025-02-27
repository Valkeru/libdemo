package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.IntegrityViolationException;

public final class CycleViolationException extends IntegrityViolationException {

    private static final String HAS_BOOKS = "Цикл %d содержит книги";
    private static final String HAS_SERIES = "Цикл %d содержит серии";

    private CycleViolationException(String message) {
        super(message);
    }

    public static CycleViolationException cycleContainsBooks(Long id) {
        return new CycleViolationException(String.format(HAS_BOOKS, id));
    }

    public static CycleViolationException cycleContainsSeries(Long id) {
        return new CycleViolationException(String.format(HAS_SERIES, id));
    }
}
