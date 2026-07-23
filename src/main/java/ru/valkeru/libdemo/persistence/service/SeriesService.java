package ru.valkeru.libdemo.persistence.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;
import ru.valkeru.libdemo.persistence.entity.Cycle;
import ru.valkeru.libdemo.persistence.entity.Series;

import java.util.Optional;
import java.util.UUID;

public interface SeriesService {

    Series createSeries(SeriesEditDto dto, Cycle cycle);

    void updateSeries(SeriesEditDto dto, Cycle cycle, Series series);

    Page<Series> listAllSeries(Pageable pageable);

    Optional<Series> getReference(UUID id);

    Optional<Series> findSeries(UUID id);

    void deleteSeriesById(UUID id);
}
