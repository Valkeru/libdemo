package ru.valkeru.libdemo.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {
        CycleMapper.class
    }
)
public interface SeriesMapper {

    SeriesDto toDto(Series entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "cycle", source = "cycle")
    void updateSeries(SeriesDto dto, Cycle cycle, @MappingTarget Series entity);
}
