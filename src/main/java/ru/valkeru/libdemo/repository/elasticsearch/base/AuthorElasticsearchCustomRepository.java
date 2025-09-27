package ru.valkeru.libdemo.repository.elasticsearch.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

import java.util.List;

public interface AuthorElasticsearchCustomRepository {

    Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable);

    AuthorDto getAuthorById(Long id);
}
