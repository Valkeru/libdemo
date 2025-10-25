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
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.web.api.DefaultApi;
import ru.valkeru.libdemo.web.api.definition.ApiDefinition;

@RestController
@RequestMapping("/security")
public interface SecurityApi extends DefaultApi {

    @Operation(
            summary = ApiDefinition.Summary.Security.SIGN_UP,
            tags = ApiDefinition.Tags.SECURITY
    )
    @PostMapping("/sign-up")
    default ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return defaultApiResponse();
    }

    @Operation(
            summary = ApiDefinition.Summary.Security.SIGN_IN,
            tags = ApiDefinition.Tags.SECURITY
    )
    @PostMapping("/sign-in")
    default ResponseEntity<TokenDto> signIn(@RequestBody SignUpRequest request) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Выполнить ротацию access токена",
            tags = ApiDefinition.Tags.SECURITY
    )
    @PostMapping("/refresh-token")
    default ResponseEntity<TokenDto> refreshToken(@RequestHeader(name = "Refresh-Token") String refreshToken) {
        return defaultApiResponse();
    }

    @Operation(
            summary = "Завершить все сессии, кроме текущей",
            tags = ApiDefinition.Tags.SECURITY,
            security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PostMapping("/revoke-sessions")
    default ResponseEntity<Void> revokeTokens(@Parameter(in = ParameterIn.HEADER, name = OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, hidden = true)
                                              @RequestHeader(name = OpenApiConfig.ACCESS_TOKEN_HEADER_NAME) String token) {
        return defaultApiResponse();
    }
}
