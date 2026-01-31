package ru.valkeru.libdemo.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface SeriesMapper {

//    @Mapping(target = "cycle", ignore = true)
    SeriesDto toDto(Series entity);

    List<SeriesDto> toDtoList(Collection<Series> seriesCollection);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "cycle", source = "cycle")
    void updateSeries(SeriesDto dto, Cycle cycle, @MappingTarget Series entity);
}
