package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorMapper {

    AuthorDto toDto(Author entity);

    AuthorDto toDto(AuthorDocument document);

    AuthorDocument toDocument(Author entity);

    List<AuthorDto> toDtoList(Collection<Author> authorCollection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateAuthor(AuthorDto dto, @MappingTarget Author entity);
}
