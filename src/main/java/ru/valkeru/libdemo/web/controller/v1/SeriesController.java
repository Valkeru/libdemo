package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.web.api.v1.SeriesApi;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.service.application.SeriesApplicationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SeriesController implements SeriesApi {

    private final SeriesApplicationService seriesService;

    @Override
    public ResponseEntity<List<SeriesDto>> listAllSeries() {
        return ResponseEntity.ok(seriesService.listAllSeries().getContent());
    }

    @Override
    public ResponseEntity<SeriesDto> getSeries(UUID id) {
        SeriesDto series = seriesService.getSeriesById(id);

        return ResponseEntity.ok(series);
    }
}
