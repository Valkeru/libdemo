package ru.valkeru.libdemo.api.v1.security;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.api.definition.ApiDefinition;
import ru.valkeru.libdemo.security.dto.SignUpRequest;

@RestController
@RequestMapping("/security")
public interface SecurityApi {

    @Operation(
            summary = ApiDefinition.Summary.Security.SIGN_UP,
            tags = ApiDefinition.Tags.SECURITY
    )
    @PostMapping("/sign-up")
    ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest);
}
