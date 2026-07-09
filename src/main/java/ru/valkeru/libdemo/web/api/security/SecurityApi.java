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
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.config.api.ApiTags;

@RequestMapping("/security")
public interface SecurityApi {

    @Operation(
            summary = "New user sign up",
            tags = ApiTags.SECURITY
    )
    @PostMapping("/sign-up")
    ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest);

    @Operation(
            summary = "User logging in",
            tags = ApiTags.SECURITY
    )
    @PostMapping("/sign-in")
    ResponseEntity<TokenDto> signIn(@RequestBody SignUpRequest request);

    @Operation(
            summary = "Rotate an access token",
            tags = ApiTags.SECURITY
    )
    @PostMapping("/refresh-token")
    ResponseEntity<TokenDto> refreshToken(@RequestHeader(name = CustomHeaders.REFRESH_TOKEN) String refreshToken);

    @Operation(
            summary = "Terminate all sessions except current session",
            tags = ApiTags.SECURITY,
            security = @SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PostMapping("/revoke-sessions")
    ResponseEntity<Void> revokeTokens(@Parameter(in = ParameterIn.HEADER, name = OpenApiConfig.ACCESS_TOKEN_HEADER_NAME, hidden = true)
                                              @RequestHeader(name = OpenApiConfig.ACCESS_TOKEN_HEADER_NAME) String token);
}
