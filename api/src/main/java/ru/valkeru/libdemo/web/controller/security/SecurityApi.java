package ru.valkeru.libdemo.web.controller.security;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.web.api.definition.ApiDefinition;

//@RestController
//@RequestMapping("/security")
public interface SecurityApi {

    @Operation(
            summary = ApiDefinition.Summary.Security.SIGN_UP,
            tags = ApiDefinition.Tags.SECURITY
    )
    @PostMapping("/sign-up")
    ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest);
}
