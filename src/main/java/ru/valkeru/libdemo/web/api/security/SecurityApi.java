package ru.valkeru.libdemo.web.api.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.config.api.ApiTags;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.util.Set;

public interface SecurityApi {

    @Operation(
        summary = "New user sign up",
        tags = ApiTags.SECURITY,
        responses = {
            @ApiResponse(responseCode = "204", description = "Success", headers = {
                @Header(name = ApiConfig.ACCESS_TOKEN_HEADER_NAME, ref = ApiConfig.ACCESS_TOKEN_HEADER_REF),
                @Header(name = ApiConfig.REFRESH_TOKEN_HEADER_NAME, ref = ApiConfig.REFRESH_TOKEN_HEADER_REF)
            })
        }
    )
    @PostMapping("/security/sign-up")
    ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest);

    @Operation(
        summary = "User logging in",
        tags = ApiTags.SECURITY,
        responses = {
            @ApiResponse(responseCode = "204", description = "Success", headers = {
                @Header(name = ApiConfig.ACCESS_TOKEN_HEADER_NAME, ref = ApiConfig.ACCESS_TOKEN_HEADER_REF),
                @Header(name = ApiConfig.REFRESH_TOKEN_HEADER_NAME, ref = ApiConfig.REFRESH_TOKEN_HEADER_REF)
            })
        }
    )
    @PostMapping("/security/sign-in")
    ResponseEntity<Void> signIn(@RequestBody SignUpRequest request);

    @Operation(
        summary = "Rotate an access token",
        tags = ApiTags.SECURITY,
        responses = {
            @ApiResponse(responseCode = "204", description = "Success", headers = {
                @Header(name = ApiConfig.ACCESS_TOKEN_HEADER_NAME, ref = ApiConfig.ACCESS_TOKEN_HEADER_REF),
                @Header(name = ApiConfig.REFRESH_TOKEN_HEADER_NAME, ref = ApiConfig.REFRESH_TOKEN_HEADER_REF)
            })
        }
    )
    @PostMapping("/security/refresh-token")
    ResponseEntity<Void> refreshToken(@RequestHeader(name = ApiConfig.REFRESH_TOKEN_HEADER_NAME) String refreshToken);

    @Operation(
        summary = "Terminate all sessions except current session",
        tags = ApiTags.SECURITY,
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME)
    )
    @PostMapping("/security/revoke-sessions")
    ResponseEntity<Void> revokeTokens(HttpServletRequest request);

    @Operation(
        summary = "Set permissions for role",
        tags = ApiTags.SECURITY,
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME),
        responses = {
            @ApiResponse(responseCode = "204", description = "Success")
        }
    )
    @PutMapping("/admin/security/permissions/{role}")
    ResponseEntity<Void> updateRolePermissions(@PathVariable Role role,
                                               @RequestBody Set<Permission> permissions);

    @Operation(
        summary = "Get permissions for role",
        tags = ApiTags.SECURITY,
        security = @SecurityRequirement(name = ApiConfig.ACCESS_TOKEN_SCHEME),
        responses = {
            @ApiResponse(responseCode = "200", description = "Success")
        }
    )
    @GetMapping("/admin/security/permissions/{role}")
    ResponseEntity<Set<Permission>> getRolePermissions(@PathVariable Role role);
}
