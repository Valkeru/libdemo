package ru.valkeru.libdemo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.SeriesNotFoundException;
import ru.valkeru.libdemo.mapper.SeriesMapper;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.repository.jpa.SeriesRepository;
import ru.valkeru.libdemo.service.base.SeriesService;
import ru.valkeru.libdemo.util.SqlUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeriesServiceImpl implements SeriesService {

    SeriesRepository seriesRepository;
    SeriesMapper seriesMapper;

    @Transactional
    @Override
    public SeriesDto createOrUpdateSeries(SeriesDto dto) {
        Series seriesEntity = getSeriesEntity(dto);
        seriesMapper.updateSeries(dto, null, seriesEntity);

        return seriesMapper.toDto(seriesRepository.save(seriesEntity));
    }

    @Override
    public List<SeriesDto> listAllSeries() {
        return seriesMapper.toDtoList(seriesRepository.findAll(SqlUtil.sortByIdAsc()));
    }

    @NonNull
    @Override
    public SeriesDto getSeries(@NonNull Long id) {
        return seriesMapper.toDto(getSeriesEntity(id));
    }

    @Transactional
    @Override
    public void deleteSeriesById(@NonNull Long id) {
        if (seriesRepository.deleteSeriesById(id) == 0) {
            throw SeriesNotFoundException.seriesNotFound(id);
        }
    }

    @NonNull
    @Override
    public Series getSeriesEntity(Long id) {
        return Optional.ofNullable(id)
                .map(seriesId -> seriesRepository.findById(seriesId)
                        .orElseThrow(() -> SeriesNotFoundException.seriesNotFound(seriesId)))
                .orElse(new Series());
    }

    private Series getSeriesEntity(SeriesDto dto) {
        return getSeriesEntity(dto.getId());
    }
}
