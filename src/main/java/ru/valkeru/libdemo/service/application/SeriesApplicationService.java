package ru.valkeru.libdemo.service.application;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.web.PagedModel;
import ru.valkeru.libdemo.model.dto.SeriesDto;

import java.util.UUID;

public interface SeriesApplicationService {

    SeriesDto createOrUpdateSeries(SeriesDto dto);

    PagedModel<SeriesDto> listAllSeries();

    @NotNull
    SeriesDto getSeriesById(@NotNull UUID id);

    void deleteSeriesById(@NotNull UUID id);
}
