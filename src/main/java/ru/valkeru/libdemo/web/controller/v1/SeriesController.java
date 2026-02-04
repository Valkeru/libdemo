package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.web.api.v1.SeriesApi;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.service.application.SeriesApplicationService;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SeriesController implements SeriesApi {

    private final SeriesApplicationService seriesService;

    @Override
    public ResponseEntity<SeriesDto> createSeries(SeriesDto series) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seriesService.createOrUpdateSeries(series));
    }

    @Override
    public ResponseEntity<SeriesDto> updateSeries(UUID id, SeriesDto series) {
        series.setId(id);

        return ResponseEntity.ok(seriesService.createOrUpdateSeries(series));
    }

    @Override
    public ResponseEntity<List<SeriesDto>> listAllSeries() {
        return ResponseEntity.ok(seriesService.listAllSeries().getContent());
    }

    @Override
    public ResponseEntity<SeriesDto> getSeries(UUID id) {
        SeriesDto series = seriesService.getSeriesById(id);

        return ResponseEntity.ok(series);
    }

    @Override
    public ResponseEntity<Void> deleteSeries(UUID id) {
        seriesService.deleteSeriesById(id);

        return ResponseEntity.noContent().build();
    }
}
