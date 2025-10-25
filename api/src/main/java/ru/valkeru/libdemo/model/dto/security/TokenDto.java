package ru.valkeru.libdemo.model.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(name = "Токен для доступа к api")
public class TokenDto {

    /**
     * JWT
     */
    private String accessToken;

    /**
     * Refresh
     */
    private String refreshToken;
}
