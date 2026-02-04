package ru.valkeru.libdemo.service.core.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.exception.impl.CycleNotFoundException;
import ru.valkeru.libdemo.mapper.CycleMapper;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.repository.jpa.CycleRepository;
import ru.valkeru.libdemo.service.core.CycleService;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CycleServiceImpl implements CycleService {

    private final CycleRepository repository;
    private final CycleMapper mapper;

    @Override
    public Cycle createOrUpdateCycle(CycleDto dto) {
        Cycle entity = getCycleEntity(dto);
        mapper.updateCycle(dto, entity);

        return dto.getId() == null
                ? repository.persist(entity)
                : repository.update(entity);
    }

    @Override
    public Page<Cycle> getAllCycles() {
        return repository.findAll(Pageable.unpaged());
    }

    @Override
    public Cycle getCycleById(UUID id) {
        return getCycleEntity(id);
    }

    @Override
    public void deleteCycleById(UUID id) {
        if (repository.deleteCycleById(id) == 0) {
            throw CycleNotFoundException.cycleNotFound(id);
        }
    }

    private Cycle getCycleEntity(CycleDto dto) {
        return Optional.ofNullable(dto.getId())
                .map(this::getCycleEntity)
                .orElse(new Cycle());
    }

    private Cycle getCycleEntity(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> CycleNotFoundException.cycleNotFound(id));
    }
}
