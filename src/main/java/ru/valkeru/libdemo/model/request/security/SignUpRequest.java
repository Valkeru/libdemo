package ru.valkeru.libdemo.model.request.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.annotation.Secret;

@Getter
@Setter
@Schema(description = "Данные для регистрации")
public class SignUpRequest {

    @Schema(description = "Имя пользователя")
    @Size(max = 12, message = "Имя пользователя должно содержать не более {max} символов")
    @Size(min = 3, message = "Имя пользователя должно содержать не менее {min} символов")
    private String username;

    @Secret(absolute = true)
    @Schema(description = "Пароль")
    @Size(min = 12, message = "Пароль должен содержать не менее {min} символов")
    private String password;
}
