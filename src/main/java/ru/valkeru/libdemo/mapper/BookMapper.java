package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BookMapper {

    BookDto toDto(Book entity);

    Collection<BookDto> toDtoCollection(Collection<Book> books);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "series", source = "series")
    @Mapping(target = "cycle", source = "cycle")
    @Mapping(target = "authors", source = "authors")
    void updateBookEntity(BookDto dto, Series series, Cycle cycle, List<Author> authors, @MappingTarget Book entity);
}
