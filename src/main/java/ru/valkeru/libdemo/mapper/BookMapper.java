package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.entity.Book;

import java.util.Collection;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BookMapper {

    BookDto toDto(Book entity);

    Collection<BookDto> toDtoCollection(Collection<Book> books);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", source = "name")
    void updateBookEntity(BookDto dto, @MappingTarget Book entity);
}
