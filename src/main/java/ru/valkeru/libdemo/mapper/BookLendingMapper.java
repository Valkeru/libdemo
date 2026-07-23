package ru.valkeru.libdemo.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.valkeru.libdemo.model.dto.internal.BookLendingCreateRequest;
import ru.valkeru.libdemo.model.dto.internal.BookLendingUpdateRequest;
import ru.valkeru.libdemo.model.dto.lending.BookLendingDto;
import ru.valkeru.libdemo.persistence.entity.BookLending;
import ru.valkeru.libdemo.persistence.projection.BookLendingListProjection;
import ru.valkeru.libdemo.model.dto.lending.BookLendingListDto;
import ru.valkeru.libdemo.persistence.projection.BookLendingProjection;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookLendingMapper {

    BookLendingListDto toListDto(BookLendingListProjection projection);

    BookLendingDto toDto(BookLendingProjection lending);

    @Mapping(target = "bookId", source = "bookInstance.book.id")
    BookLendingDto toDto(BookLending lending);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "borrowedAt", ignore = true)
    @Mapping(target = "returnedAt", ignore = true)
    BookLending create(BookLendingCreateRequest request);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void update(BookLendingUpdateRequest request, @MappingTarget BookLending lending);
}
