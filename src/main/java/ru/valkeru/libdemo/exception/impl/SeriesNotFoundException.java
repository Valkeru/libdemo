package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.NotFoundException;

public final class SeriesNotFoundException extends NotFoundException {

    private static final String SERIES_NOT_FOUND = "Серия с ID %d не найдена";

    private SeriesNotFoundException(String message) {
        super(message);
    }

    public static SeriesNotFoundException seriesNotFound(Long id) {
        return new SeriesNotFoundException(String.format(SERIES_NOT_FOUND, id));
    }
}
