package ru.valkeru.libdemo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.persistence.enums.ReadersCardInactivityReason;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Readers card")
public class ReadersCardServiceDto {

    @Schema(description = "ID")
    private UUID id;

    @Schema(description = "Card number", example = "202600001")
    private long number;

    @Schema(description = "Activity status")
    private boolean active;

    @Schema(description = "Reason why the card is inactive")
    private ReadersCardInactivityReason inactivityReason;

    @Schema(description = "Validity date start")
    private Instant validFrom;

    @Schema(description = "Validity date end")
    private Instant validTo;
}
