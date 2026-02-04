package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.web.api.v1.CycleApi;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.service.application.CycleApplicationService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CycleController implements CycleApi {

    private final CycleApplicationService cycleApplicationService;

    @Override
    public ResponseEntity<CycleDto> addCycle(CycleDto cycleDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cycleApplicationService.createOrUpdateCycle(null, cycleDto));
    }

    @Override
    public ResponseEntity<CycleDto> updateCycle(UUID id, CycleDto cycleDto) {
        return ResponseEntity.ok(cycleApplicationService.createOrUpdateCycle(id, cycleDto));
    }

    @Override
    public ResponseEntity<CycleDto> getCycle(UUID id) {
        return ResponseEntity.ok(cycleApplicationService.getCycleById(id));
    }

    @Override
    public ResponseEntity<PagedModel<CycleDto>> listCycles(Pageable pageable) {
        return ResponseEntity.ok(cycleApplicationService.getAllCycles(pageable));
    }

    @Override
    public ResponseEntity<Void> deleteCycle(UUID id) {
        cycleApplicationService.deleteCycleById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
