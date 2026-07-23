package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.persistence.entity.Author;
import ru.valkeru.libdemo.persistence.entity.Book;
import ru.valkeru.libdemo.persistence.entity.Cycle;
import ru.valkeru.libdemo.persistence.entity.Series;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.transport.BookListDto;
import ru.valkeru.libdemo.persistence.projection.BookShortProjection;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {
        JsonNullableMapper.class
    }
)
public interface BookMapper {

    BookListDto toListDto(BookShortProjection projection);

    BookDto toDto(Book entity);

    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "series", source = "series")
    @Mapping(target = "cycle", source = "cycle")
    void updateBookEntity(BookDto dto, List<Author> authors, Series series, Cycle cycle, @MappingTarget Book entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookId", ignore = true)
    BookInstanceCreateDto toDto(BookInstancePatchDto patchDto);
}
