package ru.valkeru.libdemo.service.core;

import org.springframework.data.domain.Page;
import org.jspecify.annotations.NonNull;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.UUID;

public interface SeriesService {

    Series createOrUpdateSeries(SeriesDto dto, Cycle cycle, Series series);

    Page<Series> listAllSeries();

    Series getSeries(@NonNull UUID id);

    void deleteSeriesById(@NonNull UUID id);
}
