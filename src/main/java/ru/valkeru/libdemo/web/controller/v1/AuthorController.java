package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.service.infrastructure.application.AuthorApplicationService;
import ru.valkeru.libdemo.web.api.v1.AuthorApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthorController implements AuthorApi {

    private final AuthorApplicationService authorService;

    @Override
    public ResponseEntity<Page<AuthorDto>> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        Page<AuthorDto> page = authorService.listAllAuthors(filter, pageable);

        return ResponseEntity.ok(page);
    }

    @Override
    public ResponseEntity<AuthorDto> getAuthor(UUID id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }
}
