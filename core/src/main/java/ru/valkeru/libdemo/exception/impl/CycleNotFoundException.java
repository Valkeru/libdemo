package ru.valkeru.libdemo.exception.impl;

import ru.valkeru.libdemo.exception.NotFoundException;

import java.util.UUID;

public final class CycleNotFoundException extends NotFoundException {

    private static final String CYCLE_NOT_FOUND = "Цикл %s не найден";

    private CycleNotFoundException(String message) {
        super(message);
    }

    public static CycleNotFoundException cycleNotFound(UUID id) {
        return new CycleNotFoundException(String.format(CYCLE_NOT_FOUND, id));
    }
}
