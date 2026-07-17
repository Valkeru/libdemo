package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.application.SeriesApplicationService;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;
import ru.valkeru.libdemo.web.api.v1.SeriesApi;
import ru.valkeru.libdemo.web.api.service.SeriesServiceApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class SeriesServiceController implements SeriesServiceApi {

    private final SeriesApplicationService seriesService;

    @Override
    public ResponseEntity<Void> createSeries(SeriesEditDto series) {
        UUID createdId = seriesService.createSeries(series);

        return ResponseEntity.created(
                MvcUriComponentsBuilder
                    .fromMethodCall(on(SeriesApi.class).getSeries(createdId))
                    .build().toUri()
            )
            .build();
    }

    @Override
    public ResponseEntity<Void> updateSeries(UUID id, SeriesEditDto series) {
        seriesService.updateSeries(id, series);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteSeries(UUID id) {
        seriesService.deleteSeriesById(id);

        return ResponseEntity.noContent().build();
    }
}
