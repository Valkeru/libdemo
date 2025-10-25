package ru.valkeru.libdemo.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface DefaultApi {

    default <T> ResponseEntity<T> defaultApiResponse() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
