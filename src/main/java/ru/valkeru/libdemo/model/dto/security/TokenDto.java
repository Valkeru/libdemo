package ru.valkeru.libdemo.model.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.valkeru.libdemo.annotation.Secret;

@Getter
@Setter
@Builder
@Schema(description = "API access token")
public class TokenDto {

    /**
     * JWT
     */
    @Secret
    private String accessToken;

    /**
     * Refresh
     */
    @Secret
    private String refreshToken;
}
