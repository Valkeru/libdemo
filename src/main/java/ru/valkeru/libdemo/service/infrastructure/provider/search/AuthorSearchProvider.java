package ru.valkeru.libdemo.service.infrastructure.provider.search;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

public interface AuthorSearchProvider {

    Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable);
}
