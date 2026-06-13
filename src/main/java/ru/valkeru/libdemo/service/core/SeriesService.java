package ru.valkeru.libdemo.service.core;

import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.Optional;
import java.util.UUID;

public interface SeriesService {

    Series createSeries(SeriesDto dto, Cycle cycle);

    void updateSeries(SeriesDto dto, Cycle cycle, Series series);

    Page<Series> listAllSeries();

    Optional<Series> getReference(UUID id);

    Optional<Series> findSeries(UUID id);

    void deleteSeriesById(UUID id);
}
