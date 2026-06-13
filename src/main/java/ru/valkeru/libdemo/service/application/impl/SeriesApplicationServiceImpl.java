package ru.valkeru.libdemo.service.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.CycleNotFoundException;
import ru.valkeru.libdemo.exception.impl.SeriesNotFoundException;
import ru.valkeru.libdemo.mapper.SeriesMapper;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.service.core.CycleService;
import ru.valkeru.libdemo.service.core.SeriesService;
import ru.valkeru.libdemo.service.application.SeriesApplicationService;

import java.util.Optional;
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
    public UUID createSeries(SeriesDto dto) {
        Cycle cycle = getCycle(dto.getCycle());

        return seriesService.createSeries(dto, cycle).getId();
    }

    @Override
    @Transactional
    public void updateSeries(SeriesDto dto) {
        UUID id = dto.getId();
        Series series = seriesService.findSeries(id)
            .orElseThrow(() -> SeriesNotFoundException.seriesNotFound(id));
        Cycle cycle = getCycle(dto.getCycle());

        seriesService.updateSeries(dto, cycle, series);
    }

    @Override
    public Page<SeriesDto> listAllSeries() {
        Page<Series> seriesList = seriesService.listAllSeries();

        return seriesList.map(seriesMapper::toDto);
    }

    @Override
    public SeriesDto getSeriesById(UUID id) {
        Series series = seriesService.findSeries(id)
            .orElseThrow(() -> SeriesNotFoundException.seriesNotFound(id));

        return seriesMapper.toDto(series);
    }

    @Override
    @Transactional
    public void deleteSeriesById(UUID id) {
        seriesService.deleteSeriesById(id);
    }

    private Cycle getCycle(CycleDto cycleDto) {
        UUID cycleId = Optional.ofNullable(cycleDto)
            .map(CycleDto::getId)
            .orElse(null);

        if (cycleId == null) {
            return null;
        }

        return cycleService.getReference(cycleId)
            .orElseThrow(() -> CycleNotFoundException.cycleNotFound(cycleId));
    }
}
