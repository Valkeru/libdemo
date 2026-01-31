package ru.valkeru.libdemo.web.controller.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.service.application.AuthorApplicationService;
import ru.valkeru.libdemo.web.api.service.AuthorServiceApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthorServiceController implements AuthorServiceApi {

    private final AuthorApplicationService authorService;

    @Override
    public ResponseEntity<AuthorDto> createAuthor(@Valid AuthorDto author) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.createOrUpdateAuthor(author));

    }

    @Override
    public ResponseEntity<AuthorDto> updateAuthor(UUID id, @Valid AuthorDto author) {
        author.setId(id);

        return ResponseEntity.ok(authorService.createOrUpdateAuthor(author));
    }

    @Override
    public ResponseEntity<Void> deleteAuthor(UUID id) {
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }
}
