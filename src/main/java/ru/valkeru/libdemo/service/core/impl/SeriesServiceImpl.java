package ru.valkeru.libdemo.service.core.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.exception.impl.SeriesNotFoundException;
import ru.valkeru.libdemo.mapper.SeriesMapper;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.repository.jpa.SeriesRepository;
import ru.valkeru.libdemo.service.core.SeriesService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final SeriesMapper seriesMapper;

    @Override
    public Series createOrUpdateSeries(SeriesDto dto, Cycle cycle, Series series) {
        seriesMapper.updateSeries(dto, cycle, series);

        return dto.getId() == null
                ? seriesRepository.persist(series)
                : seriesRepository.merge(series);
    }

    @Override
    public Page<Series> listAllSeries() {
        return seriesRepository.findAll(Pageable.unpaged());
    }

    @Override
    public Series getSeries(UUID id) {
        return id != null
                ? seriesRepository.findById(id)
                    .orElseThrow(() -> SeriesNotFoundException.seriesNotFound(id))
                : new Series();
    }

    @Override
    public void deleteSeriesById(UUID id) {
        if (seriesRepository.deleteSeriesById(id) == 0) {
            throw SeriesNotFoundException.seriesNotFound(id);
        }
    }
}
