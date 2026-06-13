package ru.valkeru.libdemo.model.request.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.annotation.Secret;

@Getter
@Setter
@Schema(description = "Registration data")
public class SignUpRequest {

    @Schema(description = "User name (login)")
    @Size(max = 12, message = "Login should be no more {max} characters long")
    @Size(min = 3, message = "Login should be at least {min} characters long")
    private String username;

    @Secret(absolute = true)
    @Schema(description = "Password")
    @Size(min = 12, message = "Password should be at least {min} characters long")
    private String password;
}
