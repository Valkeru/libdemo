package ru.valkeru.libdemo.exception;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String message) {
        super(message);
    }

    protected static String joinIdCollection(Collection<Long> ids) {
        return ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}
