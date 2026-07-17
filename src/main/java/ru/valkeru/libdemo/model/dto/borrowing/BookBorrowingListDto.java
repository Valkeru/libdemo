package ru.valkeru.libdemo.model.dto.borrowing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.domain.enums.BorrowingStatus;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Book borrowing info")
public class BookBorrowingListDto {

    @Schema(description = "Borrowing ID", example = "08952827-8015-47b6-900f-95af16e7bf81")
    private UUID id;

    @Schema(description = "Book ID", example = "2b11a53c-ae75-4a7c-b05c-8b362b9d5a93")
    private UUID bookId;

    @Schema(description = "Borrowing status")
    private BorrowingStatus status;
}
