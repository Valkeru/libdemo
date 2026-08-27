package ru.valkeru.libdemo.domain.projection;

import ru.valkeru.libdemo.domain.enums.LendingStatus;

import java.util.UUID;

public record BookLendingListProjection(
    UUID id,
    UUID bookId,
    LendingStatus status
) {
}
