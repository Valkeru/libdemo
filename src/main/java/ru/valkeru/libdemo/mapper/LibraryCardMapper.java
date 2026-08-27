package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.valkeru.libdemo.domain.entity.LibraryCard;
import ru.valkeru.libdemo.model.dto.LibraryCardDto;
import ru.valkeru.libdemo.model.dto.LibraryCardListDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LibraryCardMapper {

    LibraryCardDto toDto(LibraryCard libraryCard);

    LibraryCardListDto toListDto(LibraryCard libraryCard);
}
