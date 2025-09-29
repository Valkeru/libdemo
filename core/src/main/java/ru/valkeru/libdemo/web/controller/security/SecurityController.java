package ru.valkeru.libdemo.web.controller.security;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;

@Component
public class SecurityController implements SecurityApi {

    @Override
    public ResponseEntity<Void> signUp(SignUpRequest signUpRequest) {
        return null;
    }
}
