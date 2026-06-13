package ru.valkeru.libdemo.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface LibraryCommonApi {

    /**
     * Method to get "not implemented" response for endpoints not implemented in controller yet
     */
    default <T> ResponseEntity<T> defaultApiResponse() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
