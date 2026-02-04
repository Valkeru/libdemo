package ru.valkeru.libdemo.service.core;

import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.UUID;

public interface SeriesService {

    Series createOrUpdateSeries(SeriesDto dto, Cycle cycle, Series series);

    Page<Series> listAllSeries();

    /**
     * Возвращает серию с заданным ID. Если ID == null, возвращает новую сущность
     */
    Series getSeries(UUID id);

    void deleteSeriesById(UUID id);
}
