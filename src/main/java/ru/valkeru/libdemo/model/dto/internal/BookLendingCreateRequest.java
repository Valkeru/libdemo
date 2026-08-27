package ru.valkeru.libdemo.model.dto.internal;

import lombok.Builder;
import lombok.Getter;
import ru.valkeru.libdemo.domain.entity.BookInstance;
import ru.valkeru.libdemo.domain.entity.LibraryCard;
import ru.valkeru.libdemo.domain.enums.LendingStatus;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class BookLendingCreateRequest {

    private BookInstance bookInstance;

    private LibraryCard libraryCard;

    private Instant reservedAt;

    private LocalDate reservationDueDate;

    private LocalDate returnDueDate;

    private LendingStatus status;
}
