package ru.valkeru.libdemo.service.application;

import org.springframework.data.web.PagedModel;
import ru.valkeru.libdemo.model.dto.SeriesDto;

import java.util.UUID;

public interface SeriesApplicationService {

    SeriesDto createOrUpdateSeries(SeriesDto dto);

    PagedModel<SeriesDto> listAllSeries();

    SeriesDto getSeriesById(UUID id);

    void deleteSeriesById(UUID id);
}
