package ru.valkeru.libdemo.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.BookInstance;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceListDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceViewDto;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BookInstanceMapper {

    @Mapping(target = "bookId", source = "book.id")
    BookInstanceViewDto toDto(BookInstance instance);

    void update(BookInstanceCreateDto dto, Book book, @MappingTarget BookInstance instance);

    @Mapping(target = "book", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(BookInstanceCreateDto dto, @MappingTarget BookInstance instance);

    BookInstanceListDto toListDto(BookInstance bookInstance);
}
