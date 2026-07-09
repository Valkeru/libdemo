package ru.valkeru.libdemo.service.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.CycleNotFoundException;
import ru.valkeru.libdemo.exception.impl.SeriesNotFoundException;
import ru.valkeru.libdemo.mapper.SeriesMapper;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;
import ru.valkeru.libdemo.domain.entity.Cycle;
import ru.valkeru.libdemo.domain.entity.Series;
import ru.valkeru.libdemo.domain.service.CycleService;
import ru.valkeru.libdemo.domain.service.SeriesService;
import ru.valkeru.libdemo.service.application.SeriesApplicationService;

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
    public UUID createSeries(SeriesEditDto dto) {
        Cycle cycle = getCycle(dto.getCycleId());

        return seriesService.createSeries(dto, cycle).getId();
    }

    @Override
    @Transactional
    public void updateSeries(UUID id, SeriesEditDto dto) {
        Series series = seriesService.findSeries(id)
            .orElseThrow(() -> SeriesNotFoundException.seriesNotFound(id));
        Cycle cycle = getCycle(dto.getCycleId());

        seriesService.updateSeries(dto, cycle, series);
    }

    @Override
    public Page<SeriesDto> listAllSeries(Pageable pageable) {
        Page<Series> seriesList = seriesService.listAllSeries(pageable);

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

    private Cycle getCycle(UUID cycleId) {
        if (cycleId == null) {
            return null;
        }

        return cycleService.getReference(cycleId)
            .orElseThrow(() -> CycleNotFoundException.cycleNotFound(cycleId));
    }
}
