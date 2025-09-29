package ru.valkeru.libdemo.service.web.base;

import jakarta.validation.constraints.NotNull;
import ru.valkeru.libdemo.model.dto.SeriesDto;

import java.util.List;

public interface SeriesHttpService {

    SeriesDto createOrUpdateSeries(SeriesDto dto);

    List<SeriesDto> listAllSeries();

    @NotNull
    SeriesDto getSeriesById(@NotNull Long id);

    void deleteSeriesById(@NotNull Long id);
}
