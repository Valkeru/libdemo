package ru.valkeru.libdemo.service.application;

import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.SeriesDto;

import java.util.UUID;

public interface SeriesApplicationService {

    UUID createSeries(SeriesDto dto);

    void updateSeries(SeriesDto dto);

    Page<SeriesDto> listAllSeries();

    SeriesDto getSeriesById(UUID id);

    void deleteSeriesById(UUID id);
}
