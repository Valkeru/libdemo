package ru.valkeru.libdemo.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Данные для регистрации")
public class SignUpRequest {

    @Schema(description = "Имя пользователя")
    @Size(max = 3, message = "Имя пользователя должно содержать не более {max} символов")
    private String username;

    @Schema(description = "Пароль")
    @Size(min = 12, message = "Пароль должен содержать не менее {min} символов")
    private String password;
}
