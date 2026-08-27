package ru.valkeru.libdemo.model.dto.internal;

import lombok.Builder;
import lombok.Getter;
import ru.valkeru.libdemo.domain.enums.LendingStatus;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class BookLendingUpdateRequest {

    private Instant borrowedAt;

    private LocalDate returnDueDate;

    private LendingStatus status;
}
