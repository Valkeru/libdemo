package ru.valkeru.libdemo.controller.v1;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.service.http.SeriesHttpService;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeriesController implements SeriesApi {

    SeriesHttpService seriesService;

    @Override
    public ResponseEntity<SeriesDto> createSeries(SeriesDto series) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seriesService.createOrUpdateSeries(series));
    }

    @Override
    public ResponseEntity<SeriesDto> updateSeries(Long id, SeriesDto series) {
        series.setId(id);

        return ResponseEntity.ok(seriesService.createOrUpdateSeries(series));
    }

    @Override
    public ResponseEntity<Collection<SeriesDto>> listAllSeries() {
        return ResponseEntity.ok(seriesService.listAllSeries());
    }

    @Override
    public ResponseEntity<SeriesDto> getSeries(Long id) {
        SeriesDto series = seriesService.getSeriesById(id);

        return ResponseEntity.ok(series);
    }

    @Override
    public ResponseEntity<Void> deleteSeries(Long id) {
        seriesService.deleteSeriesById(id);

        return ResponseEntity.noContent().build();
    }
}
