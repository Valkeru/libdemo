package ru.valkeru.libdemo.service.infrastructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.mapper.SeriesMapper;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.service.core.CycleService;
import ru.valkeru.libdemo.service.core.SeriesService;
import ru.valkeru.libdemo.service.infrastructure.application.SeriesApplicationService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeriesApplicationServiceImpl implements SeriesApplicationService {

    private final SeriesService seriesService;
    private final CycleService cycleService;

    private final SeriesMapper seriesMapper;

    @Override
    @Transactional
    public SeriesDto createOrUpdateSeries(SeriesDto dto) {
        Series series = seriesService.getSeries(dto.getId());

        Series updated = seriesService.createOrUpdateSeries(dto, getCycle(dto.getCycle()), series);

        return seriesMapper.toDto(updated);
    }

    @Override
    public PagedModel<SeriesDto> listAllSeries() {
        Page<Series> seriesList = seriesService.listAllSeries();

        return new PagedModel<>(seriesList.map(seriesMapper::toDto));
    }

    @Override
    public SeriesDto getSeriesById(UUID id) {
        Series series = seriesService.getSeries(id);

        return seriesMapper.toDto(series);
    }

    @Override
    @Transactional
    public void deleteSeriesById(UUID id) {
        seriesService.deleteSeriesById(id);
    }

    private Cycle getCycle(CycleDto dto) {
        return dto != null ? cycleService.getCycleById(dto.getId()) : null;
    }
}
