package ru.valkeru.libdemo.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.valkeru.libdemo.mapper.qualifier.CycleById;
import ru.valkeru.libdemo.mapper.util.MapperUtil;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                AuthorMapper.class,
                MapperUtil.class
        }
)
public interface SeriesMapper {

    SeriesDto toDto(Series entity);

    List<SeriesDto> toDtoList(Collection<Series> seriesCollection);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "cycle", source = "cycle.id", qualifiedBy = CycleById.class)
    void updateSeries(SeriesDto dto, @MappingTarget Series entity);
}
