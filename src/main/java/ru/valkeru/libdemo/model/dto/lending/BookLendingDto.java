package ru.valkeru.libdemo.model.dto.lending;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.valkeru.libdemo.persistence.enums.LendingStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Book lending info")
public class BookLendingDto {

    @Schema(description = "Lending ID", example = "08952827-8015-47b6-900f-95af16e7bf81")
    private UUID id;

    @Schema(description = "Book ID", example = "2b11a53c-ae75-4a7c-b05c-8b362b9d5a93")
    private UUID bookId;

    @Schema(description = "Reservation date and time", example = "2026-07-17T19:25:11.902Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant reservedAt;

    @Schema(description = "Lending date and time", example = "2026-07-17T19:25:11.902Z")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant borrowedAt;

    @Schema(description = "Returning due date", example = "2027-01-12")
    private LocalDate returnDueDate;

    @Schema(description = "Book returning date", example = "2026-07-17T19:25:11.902Z")
    private Instant returnedAt;

    @Schema(description = "Lending status")
    private LendingStatus status;
}
