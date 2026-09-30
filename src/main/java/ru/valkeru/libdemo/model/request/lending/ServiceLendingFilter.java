package ru.valkeru.libdemo.model.request.lending;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.LuhnCheck;
import ru.valkeru.libdemo.constants.ValidationConstants;

@Getter
@Setter
public class ServiceLendingFilter {

    @LuhnCheck(message = "Invalid library card number")
    @NotBlank(message = ValidationConstants.MSG_MANDATORY_FIELD)
    @Schema(description = "Library card number", requiredMode = Schema.RequiredMode.REQUIRED)
    private String libraryCardNumber;
}
