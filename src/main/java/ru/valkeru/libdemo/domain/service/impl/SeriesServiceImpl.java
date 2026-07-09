package ru.valkeru.libdemo.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.exception.impl.SeriesNotFoundException;
import ru.valkeru.libdemo.mapper.SeriesMapper;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;
import ru.valkeru.libdemo.domain.entity.Cycle;
import ru.valkeru.libdemo.domain.entity.Series;
import ru.valkeru.libdemo.domain.repository.jpa.SeriesRepository;
import ru.valkeru.libdemo.domain.service.SeriesService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository repository;
    private final SeriesMapper seriesMapper;

    @Override
    public Series createSeries(SeriesEditDto dto, Cycle cycle) {
        Series series = new Series();
        seriesMapper.updateSeries(dto, cycle, series);

        return repository.persist(series);
    }

    @Override
    public void updateSeries(SeriesEditDto dto, Cycle cycle, Series series) {
        seriesMapper.updateSeries(dto, cycle, series);

        repository.merge(series);
    }

    @Override
    public Page<Series> listAllSeries(Pageable pageable) {
        Sort sort = Sort.by("s.createdAt").descending();
        Pageable request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return repository.findAll(request);
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
