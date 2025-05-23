package ru.valkeru.libdemo.service.http;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.service.AuthorService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorHttpServiceImpl implements AuthorHttpService {

    AuthorService authorService;

    @Override
    public AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        return authorService.createOrUpdateAuthor(authorDto);
    }

    @Override
    public Collection<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        return authorService.getAuthors(filter, pageable);
    }

    @Cacheable("author")
    @Override
    public AuthorDto getAuthorById(Long id) {
        return authorService.getAuthorById(id);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorService.deleteAuthorById(id);
    }

    @Override
    public void reindexAuthors() {
        authorService.reindexAuthors();
    }
}
