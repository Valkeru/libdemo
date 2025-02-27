package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.mapper.qualifier.AuthorsByDtoList;
import ru.valkeru.libdemo.mapper.qualifier.CycleById;
import ru.valkeru.libdemo.mapper.qualifier.SeriesById;
import ru.valkeru.libdemo.mapper.util.MapperUtil;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.entity.Book;

import java.util.Collection;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                MapperUtil.class,
                AuthorMapper.class,
                SeriesMapper.class
        }
)
public interface BookMapper {

    BookDto toDto(Book entity);

    Collection<BookDto> toDtoCollection(Collection<Book> books);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "series", source = "series.id", qualifiedBy = SeriesById.class)
    @Mapping(target = "cycle", source = "cycle.id", qualifiedBy = CycleById.class)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "authors", source = "authors", qualifiedBy = AuthorsByDtoList.class)
    void updateBookEntity(BookDto dto, @MappingTarget Book entity);
}
