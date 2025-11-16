package ru.valkeru.libdemo.web.service.base;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.SeriesDto;

import java.util.UUID;

public interface SeriesWebService {

    SeriesDto createOrUpdateSeries(SeriesDto dto);

    Page<SeriesDto> listAllSeries();

    @NotNull
    SeriesDto getSeriesById(@NotNull UUID id);

    void deleteSeriesById(@NotNull UUID id);
}
