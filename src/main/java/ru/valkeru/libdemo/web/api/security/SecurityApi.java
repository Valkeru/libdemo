package ru.valkeru.libdemo.web.api.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.web.api.definition.ApiTags;

@RequestMapping("/security")
public interface SecurityApi extends DefaultApi {

    @Operation(
            summary = "Регистрация",
            tags = ApiTags.SECURITY
    )
    @PostMapping("/sign-up")
    default ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Вход",
            tags = ApiTags.SECURITY
    )
    @PostMapping("/sign-in")
    default ResponseEntity<TokenDto> signIn(@RequestBody SignUpRequest request) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Выполнить ротацию access токена",
            tags = ApiTags.SECURITY
    )
    @PostMapping("/refresh-token")
    default ResponseEntity<TokenDto> refreshToken(@RequestHeader(name = "Refresh-Token") String refreshToken) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Завершить все сессии, кроме текущей",
            tags = ApiTags.SECURITY,
            security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PostMapping("/revoke-sessions")
    default ResponseEntity<Void> revokeTokens(@Parameter(in = ParameterIn.HEADER, name = OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, hidden = true)
                                              @RequestHeader(name = OpenApiConfig.ACCESS_TOKEN_HEADER_NAME) String token) {
        return defaultApiResponse();
    }
}
