package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.NotFoundException;

import java.util.UUID;

public final class SeriesNotFoundException extends NotFoundException {

    private static final String SERIES_NOT_FOUND = "Series with ID %s not found";

    private SeriesNotFoundException(String message) {
        super(message);
    }

    public static SeriesNotFoundException seriesNotFound(UUID id) {
        return new SeriesNotFoundException(String.format(SERIES_NOT_FOUND, id));
    }
}
