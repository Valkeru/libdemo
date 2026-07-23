package ru.valkeru.libdemo.persistence.projection;

import ru.valkeru.libdemo.persistence.enums.LendingStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BookLendingProjection(
    UUID id,
    UUID bookId,
    Instant reservedAt,
    Instant borrowedAt,
    LocalDate returnDueDate,
    Instant returnedAt,
    LendingStatus status
) {
}
