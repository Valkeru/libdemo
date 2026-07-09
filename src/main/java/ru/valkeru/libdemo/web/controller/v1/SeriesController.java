package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.model.dto.series.SeriesEditDto;
import ru.valkeru.libdemo.web.api.service.SeriesServiceApi;
import ru.valkeru.libdemo.web.api.v1.SeriesApi;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;
import ru.valkeru.libdemo.service.application.SeriesApplicationService;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class SeriesController implements SeriesApi, SeriesServiceApi {

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
