package ru.valkeru.libdemo.service.base;

import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.List;
import java.util.UUID;

public interface SeriesService {

    Series createOrUpdateSeries(SeriesDto dto, Cycle cycle, Series series);

    Page<Series> listAllSeries();

    @NonNull
    Series getSeries(@NonNull UUID id);

    @Deprecated
    @NonNull
    Series getSeriesEntity(UUID id);

    void deleteSeriesById(@NonNull UUID id);
}
