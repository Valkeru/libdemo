package ru.valkeru.libdemo.persistence.projection;

import ru.valkeru.libdemo.persistence.enums.LendingStatus;

import java.util.UUID;

public record BookLendingListProjection(
    UUID id,
    UUID bookId,
    LendingStatus status
) {
}
