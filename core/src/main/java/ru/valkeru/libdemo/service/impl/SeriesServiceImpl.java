package ru.valkeru.libdemo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.SeriesNotFoundException;
import ru.valkeru.libdemo.mapper.SeriesMapper;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.repository.jpa.SeriesRepository;
import ru.valkeru.libdemo.service.base.SeriesService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeriesServiceImpl implements SeriesService {

    SeriesRepository seriesRepository;
    SeriesMapper seriesMapper;

    @Transactional
    @Override
    public Series createOrUpdateSeries(SeriesDto dto, Cycle cycle, Series series) {
        seriesMapper.updateSeries(dto, cycle, series);

        return seriesRepository.save(series);
    }

    @Override
    public Page<Series> listAllSeries() {
        return seriesRepository.findAll(Pageable.unpaged());
    }

    @NonNull
    @Override
    public Series getSeries(@NonNull UUID id) {
        return getSeriesEntity(id);
    }

    @Transactional
    @Override
    public void deleteSeriesById(@NonNull UUID id) {
        if (seriesRepository.deleteSeriesById(id) == 0) {
            throw SeriesNotFoundException.seriesNotFound(id);
        }
    }

    private Series getSeriesEntity(UUID id) {
        return Optional.ofNullable(id)
                .map(seriesId -> seriesRepository.findById(seriesId)
                        .orElseThrow(() -> SeriesNotFoundException.seriesNotFound(seriesId)))
                .orElse(new Series());
    }
}
