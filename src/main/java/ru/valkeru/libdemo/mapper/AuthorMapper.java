package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.domain.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.domain.entity.Author;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorMapper {

    AuthorDto toDto(Author entity);

    AuthorDto toDto(AuthorDocument document);

    AuthorDocument toDocument(AuthorDto entity);

    void updateAuthor(AuthorDto dto, @MappingTarget Author entity);
}
