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
import ru.valkeru.libdemo.model.transport.BookListDto;
import ru.valkeru.libdemo.repository.jpa.projection.BookShortProjection;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BookMapper {

    BookListDto toListDto(BookShortProjection projection);

    BookDto toDto(Book entity);

    List<BookDto> toDtoList(Collection<Book> books);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "series", source = "series")
    @Mapping(target = "cycle", source = "cycle")
    void updateBookEntity(BookDto dto, List<Author> authors, Series series, Cycle cycle, @MappingTarget Book entity);
}
