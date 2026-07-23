package ru.valkeru.libdemo.model.dto.internal;

import lombok.Builder;
import lombok.Getter;
import ru.valkeru.libdemo.persistence.entity.BookInstance;
import ru.valkeru.libdemo.persistence.entity.ReadersCard;
import ru.valkeru.libdemo.persistence.enums.LendingStatus;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class BookLendingCreateRequest {

    private BookInstance bookInstance;

    private ReadersCard readersCard;

    private Instant reservedAt;

    private LocalDate reservationDueDate;

    private LocalDate returnDueDate;

    private LendingStatus status;
}
