package ru.valkeru.libdemo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.CycleNotFoundException;
import ru.valkeru.libdemo.mapper.CycleMapper;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.repository.jpa.CycleRepository;
import ru.valkeru.libdemo.service.CycleService;
import ru.valkeru.libdemo.util.SqlUtil;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CycleServiceImpl implements CycleService {

    CycleRepository cycleRepository;
    CycleMapper cycleMapper;

    @Transactional
    @Override
    public CycleDto createOrUpdateCycle(CycleDto dto) {
        Cycle entity = getCycleEntity(dto);
        cycleMapper.updateCycle(dto, entity);

        return cycleMapper.toDto(cycleRepository.save(entity));
    }

    @Override
    public Collection<CycleDto> getAllCycles() {
        return cycleRepository.getCycles(SqlUtil.sortByIdAsc());
    }

    @Override
    public CycleDto getCycleById(Long id) {
        return cycleMapper.toDto(getCycleEntity(id));
    }

    @Override
    public Cycle getCycleEntityById(Long id) {
        return getCycleEntity(id);
    }

    @Transactional
    @Override
    public void deleteCycleById(Long id) {
        if (cycleRepository.deleteCycleById(id) == 0) {
            throw CycleNotFoundException.cycleNotFound(id);
        }
    }

    private Cycle getCycleEntity(CycleDto dto) {
        return Optional.ofNullable(dto.getId())
                .map(this::getCycleEntity)
                .orElse(new Cycle());
    }

    private Cycle getCycleEntity(Long id) {
        return cycleRepository.findById(id)
                .orElseThrow(() -> CycleNotFoundException.cycleNotFound(id));
    }
}
