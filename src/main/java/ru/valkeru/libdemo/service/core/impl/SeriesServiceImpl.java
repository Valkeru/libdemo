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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository repository;
    private final SeriesMapper seriesMapper;

    @Override
    public Series createSeries(SeriesDto dto, Cycle cycle) {
        Series series = new Series();
        seriesMapper.updateSeries(dto, cycle, series);

        return repository.persist(series);
    }

    @Override
    public void updateSeries(SeriesDto dto, Cycle cycle, Series series) {
        seriesMapper.updateSeries(dto, cycle, series);

        repository.merge(series);
    }

    @Override
    public Page<Series> listAllSeries() {
        return repository.findAll(Pageable.unpaged());
    }

    @Override
    public Optional<Series> getReference(UUID id) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }

        return Optional.of(repository.getReferenceById(id));
    }

    @Override
    public Optional<Series> findSeries(UUID id) {
        return repository.findById(id);
    }

    @Override
    public void deleteSeriesById(UUID id) {
        if (repository.deleteSeriesById(id) == 0) {
            throw SeriesNotFoundException.seriesNotFound(id);
        }
    }
}
