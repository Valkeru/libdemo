package ru.valkeru.libdemo.service;

import org.springframework.lang.NonNull;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.List;

public interface SeriesService {

    SeriesDto createOrUpdateSeries(SeriesDto dto);

    List<SeriesDto> listAllSeries();

    @NonNull
    SeriesDto getSeries(@NonNull Long id);

    @NonNull
    Series getSeriesEntity(Long id);

    void deleteSeriesById(@NonNull Long id);

    int countSeriesByCycleId(Long cycleId);
}
