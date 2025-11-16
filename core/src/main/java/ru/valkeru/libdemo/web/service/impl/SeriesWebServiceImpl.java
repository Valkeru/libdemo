package ru.valkeru.libdemo.web.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.mapper.SeriesMapper;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.service.base.CycleService;
import ru.valkeru.libdemo.service.base.SeriesService;
import ru.valkeru.libdemo.web.service.base.SeriesWebService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesWebServiceImpl implements SeriesWebService {

    private final SeriesService seriesService;
    private final CycleService cycleService;
    private final SeriesMapper seriesMapper;

    @Override
    public SeriesDto createOrUpdateSeries(SeriesDto dto) {
        Series series = seriesService.getSeries(dto.getId());

        return seriesMapper.toDto(seriesService.createOrUpdateSeries(dto, getCycle(dto.getCycle()), series));
    }

    @Override
    public Page<SeriesDto> listAllSeries() {
        Page<Series> seriesList = seriesService.listAllSeries();

        return seriesList.map(seriesMapper::toDto);
    }

    @NotNull
    @Override
    public SeriesDto getSeriesById(@NotNull UUID id) {
        Series series = seriesService.getSeries(id);

        return seriesMapper.toDto(series);
    }

    @Override
    public void deleteSeriesById(UUID id) {
        seriesService.deleteSeriesById(id);
    }

    private Cycle getCycle(CycleDto dto) {
        return dto != null ? cycleService.getCycleById(dto.getId()) : null;
    }
}
