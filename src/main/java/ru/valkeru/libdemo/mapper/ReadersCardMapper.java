package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.valkeru.libdemo.persistence.entity.ReadersCard;
import ru.valkeru.libdemo.model.dto.ReadersCardDto;
import ru.valkeru.libdemo.model.dto.ReadersCardListDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReadersCardMapper {

    ReadersCardDto toDto(ReadersCard readersCard);

    ReadersCardListDto toListDto(ReadersCard readersCard);
}
