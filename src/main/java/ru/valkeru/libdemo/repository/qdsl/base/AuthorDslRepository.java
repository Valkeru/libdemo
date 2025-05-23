package ru.valkeru.libdemo.repository.qdsl.base;

import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

import java.util.List;

public interface AuthorDslRepository {

    List<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable);

    AuthorDto getAuthorById(Long id);
}
