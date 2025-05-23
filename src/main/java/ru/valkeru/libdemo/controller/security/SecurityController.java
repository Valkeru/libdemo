package ru.valkeru.libdemo.controller.security;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.api.v1.security.SecurityApi;
import ru.valkeru.libdemo.security.dto.SignUpRequest;

@Component
public class SecurityController implements SecurityApi {

    @Override
    public ResponseEntity<Void> signUp(SignUpRequest signUpRequest) {
        return null;
    }
}
