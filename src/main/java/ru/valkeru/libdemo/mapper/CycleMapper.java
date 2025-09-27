package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CycleMapper {

    CycleDto toDto(Cycle entity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCycle(CycleDto dto, @MappingTarget Cycle cycle);
}
