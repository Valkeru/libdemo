package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.service.infrastructure.application.CycleApplicationService;
import ru.valkeru.libdemo.web.api.service.CycleServiceApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CycleServiceController implements CycleServiceApi {

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
    public ResponseEntity<Void> deleteCycle(UUID id) {
        cycleApplicationService.deleteCycleById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
