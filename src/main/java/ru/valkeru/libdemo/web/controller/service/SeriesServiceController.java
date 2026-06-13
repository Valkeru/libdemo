package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.service.application.SeriesApplicationService;
import ru.valkeru.libdemo.web.api.service.SeriesServiceApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SeriesServiceController implements SeriesServiceApi {

    private final SeriesApplicationService seriesService;

    @Override
    public ResponseEntity<Void> createSeries(SeriesDto series) {
        UUID createdId = seriesService.createSeries(series);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(CustomHeaders.RESOURCE_ID, createdId.toString())
            .build();
    }

    @Override
    public ResponseEntity<SeriesDto> updateSeries(UUID id, SeriesDto series) {
        series.setId(id);
        seriesService.updateSeries(series);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteSeries(UUID id) {
        seriesService.deleteSeriesById(id);

        return ResponseEntity.noContent().build();
    }
}
