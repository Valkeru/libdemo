package ru.valkeru.libdemo.web.controller.v1;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.web.api.v1.AuthorServiceApi;
import ru.valkeru.libdemo.service.http.AuthorHttpService;

//@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorServiceController implements AuthorServiceApi {

    AuthorHttpService service;

    @Override
    public ResponseEntity<Void> elasticsearchReindexAuthors() {
        service.reindexAuthors();

        return ResponseEntity.noContent().build();
    }
}
