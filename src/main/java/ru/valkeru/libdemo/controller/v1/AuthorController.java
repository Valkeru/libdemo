package ru.valkeru.libdemo.controller.v1;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.service.http.AuthorHttpService;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorController implements AuthorApi {

    AuthorHttpService authorService;

    @Override
    public ResponseEntity<AuthorDto> createAuthor(@Valid AuthorDto author) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorService.createOrUpdateAuthor(author));

    }

    @Override
    public ResponseEntity<AuthorDto> updateAuthor(Long id, @Valid AuthorDto author) {
        author.setId(id);

        return ResponseEntity.ok(authorService.createOrUpdateAuthor(author));
    }

    @Override
    public ResponseEntity<Collection<AuthorDto>> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        return ResponseEntity.ok(authorService.listAllAuthors(filter, pageable));
    }

    @Override
    public ResponseEntity<AuthorDto> getAuthor(Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Override
    public ResponseEntity<Void> deleteAuthor(Long id) {
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<AuthorDto>> getPageableAuthors() {
        return null;
    }
}
