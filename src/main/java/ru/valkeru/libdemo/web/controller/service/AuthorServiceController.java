package ru.valkeru.libdemo.web.controller.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.service.application.AuthorApplicationService;
import ru.valkeru.libdemo.web.api.service.AuthorServiceApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthorServiceController implements AuthorServiceApi {

    private final AuthorApplicationService authorService;

    @Override
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).AUTHOR_CREATE.name())")
    public ResponseEntity<Void> createAuthor(@Valid AuthorDto author) {
        author.setId(null);

        UUID createdAuthorID = authorService.createAuthor(author);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(CustomHeaders.RESOURCE_ID, createdAuthorID.toString())
            .build();

    }

    @Override
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).AUTHOR_UPDATE.name())")
    public ResponseEntity<Void> updateAuthor(UUID id, @Valid AuthorDto author) {
        author.setId(id);
        authorService.updateAuthor(author);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAuthority(T(ru.valkeru.libdemo.security.Permission).AUTHOR_DELETE.name())")
    public ResponseEntity<Void> deleteAuthor(UUID id) {
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> elasticsearchReindexAuthors() {
        authorService.reindexAuthors();

        return ResponseEntity.noContent().build();
    }
}
