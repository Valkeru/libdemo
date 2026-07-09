package ru.valkeru.libdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;
import ru.valkeru.libdemo.domain.entity.Cycle;
import ru.valkeru.libdemo.domain.entity.Series;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {
        CycleMapper.class
    }
)
public interface SeriesMapper {

    SeriesDto toDto(Series entity);

    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "cycle", source = "cycle")
    void updateSeries(SeriesEditDto dto, Cycle cycle, @MappingTarget Series entity);
}
