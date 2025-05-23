package ru.valkeru.libdemo.service.http;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.service.SeriesService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeriesHttpServiceImpl implements SeriesHttpService {

    SeriesService seriesService;

    @Override
    public SeriesDto createOrUpdateSeries(SeriesDto dto) {
        return seriesService.createOrUpdateSeries(dto);
    }

    @Override
    public List<SeriesDto> listAllSeries() {
        return seriesService.listAllSeries();
    }

    @NotNull
    @Override
    public SeriesDto getSeriesById(@NotNull Long id) {
        return seriesService.getSeries(id);
    }

    @Override
    public void deleteSeriesById(Long id) {
        seriesService.deleteSeriesById(id);
    }
}
