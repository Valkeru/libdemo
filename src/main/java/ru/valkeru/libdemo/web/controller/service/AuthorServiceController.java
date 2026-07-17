package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.application.AuthorApplicationService;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.web.api.v1.AuthorApi;
import ru.valkeru.libdemo.web.api.service.AuthorServiceApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.system.elasticsearch-enabled", havingValue = "false", matchIfMissing = true)
public class AuthorServiceController implements AuthorServiceApi {

    protected final AuthorApplicationService authorService;

    @Override
    public ResponseEntity<Void> createAuthor(AuthorDto author) {
        author.setId(null);

        UUID createdAuthorID = authorService.createAuthor(author);

        return ResponseEntity.created(
                MvcUriComponentsBuilder.fromMethodCall(
                    MvcUriComponentsBuilder.on(AuthorApi.class).getAuthor(createdAuthorID)
                ).build().toUri()
            )
            .build();

    }

    @Override
    public ResponseEntity<Void> updateAuthor(UUID id, AuthorDto author) {
        author.setId(id);
        authorService.updateAuthor(author);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteAuthor(UUID id) {
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }
}
