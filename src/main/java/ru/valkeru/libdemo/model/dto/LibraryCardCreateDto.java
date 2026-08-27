package ru.valkeru.libdemo.model.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.constants.ValidationConstants;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Builder
@Getter
@Setter
public class LibraryCardCreateDto {

    @Schema(description = "User ID")
    @NotNull(message = ValidationConstants.MSG_MANDATORY_FIELD)
    private UUID userId;

    @Schema(description = "Card validity date start")
    @NotNull(message = ValidationConstants.MSG_MANDATORY_FIELD)
    private Instant validFrom;

    @Schema(description = "Card validity date end")
    private Instant validTo;

    @JsonSetter
    public void setValidTo(Instant validTo) {
        this.validTo = validTo.truncatedTo(ChronoUnit.DAYS);
    }
}
