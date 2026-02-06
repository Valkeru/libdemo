package ru.valkeru.libdemo.service.infrastructure.provider.search;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

public interface AuthorSearchProvider {

    PagedModel<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable);
}
