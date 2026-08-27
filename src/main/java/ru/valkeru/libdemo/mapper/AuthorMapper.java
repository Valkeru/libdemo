package ru.valkeru.libdemo.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.valkeru.libdemo.domain.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.domain.entity.Author;
import ru.valkeru.libdemo.model.transport.AuthorListDto;

import java.util.StringJoiner;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorMapper {

    AuthorDto toDto(Author entity);

    AuthorDto toDto(AuthorDocument document);

    AuthorDocument toDocument(Author entity);

    @Mapping(target = "fullName", source = "dto", qualifiedByName = "authorFullName")
    AuthorListDto toListDto(AuthorDto dto);

    void updateAuthor(AuthorDto dto, @MappingTarget Author entity);

    @Named("authorFullName")
    default String getFullName(AuthorDto dto) {
        StringJoiner joiner = new StringJoiner(" ");

        joiner.add(dto.getFirstName());
        if (StringUtils.isNotBlank(dto.getMiddleName())) {
            joiner.add(dto.getMiddleName());
        }
        joiner.add(dto.getLastName());

        return joiner.toString();
    }
}
