package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.web.api.v1.SeriesApi;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;
import ru.valkeru.libdemo.application.SeriesApplicationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SeriesController implements SeriesApi {

    private final SeriesApplicationService seriesService;

    @Override
    public ResponseEntity<Page<SeriesDto>> listAllSeries(Pageable pageable) {
        return ResponseEntity.ok(seriesService.listAllSeries(pageable));
    }

    @Override
    public ResponseEntity<SeriesDto> getSeries(UUID id) {
        SeriesDto series = seriesService.getSeriesById(id);

        return ResponseEntity.ok(series);
    }
}
