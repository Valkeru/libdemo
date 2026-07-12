package ru.valkeru.libdemo.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;

import java.util.UUID;

public interface SeriesApplicationService {

    UUID createSeries(SeriesEditDto dto);

    void updateSeries(UUID id, SeriesEditDto dto);

    Page<SeriesDto> listAllSeries(Pageable pageable);

    SeriesDto getSeriesById(UUID id);

    void deleteSeriesById(UUID id);
}
