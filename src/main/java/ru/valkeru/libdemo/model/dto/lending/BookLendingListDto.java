package ru.valkeru.libdemo.model.dto.lending;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.persistence.enums.LendingStatus;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Book lending info")
public class BookLendingListDto {

    @Schema(description = "Lending ID", example = "08952827-8015-47b6-900f-95af16e7bf81")
    private UUID id;

    @Schema(description = "Book ID", example = "2b11a53c-ae75-4a7c-b05c-8b362b9d5a93")
    private UUID bookId;

    @Schema(description = "Lending status")
    private LendingStatus status;
}
