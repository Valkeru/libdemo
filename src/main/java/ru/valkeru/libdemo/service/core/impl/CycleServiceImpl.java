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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CycleServiceImpl implements CycleService {

    private final CycleRepository repository;
    private final CycleMapper mapper;

    @Override
    public Cycle createOrUpdateCycle(CycleDto dto) {
        Cycle entity = getCycleById(dto.getId());
        mapper.updateCycle(dto, entity);

        return dto.getId() == null
                ? repository.persist(entity)
                : repository.merge(entity);
    }

    @Override
    public Page<Cycle> getAllCycles(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Cycle getCycleById(UUID id) {
        return id != null
                ? repository.findById(id)
                    .orElseThrow(() -> CycleNotFoundException.cycleNotFound(id))
                : new Cycle();
    }

    @Override
    public void deleteCycleById(UUID id) {
        if (repository.deleteCycleById(id) == 0) {
            throw CycleNotFoundException.cycleNotFound(id);
        }
    }
}
