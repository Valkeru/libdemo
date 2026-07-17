package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.application.CycleApplicationService;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.web.api.v1.CycleApi;
import ru.valkeru.libdemo.web.api.service.CycleServiceApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class CycleServiceController implements CycleServiceApi {

    private final CycleApplicationService cycleApplicationService;

    @Override
    public ResponseEntity<Void> addCycle(CycleDto cycleDto) {
        UUID id = cycleApplicationService.createCycle(cycleDto);

        return ResponseEntity.created(
                MvcUriComponentsBuilder
                    .fromMethodCall(on(CycleApi.class).getCycle(id))
                    .build().toUri()
            )
            .build();
    }

    @Override
    public ResponseEntity<Void> updateCycle(UUID id, CycleDto cycleDto) {
        cycleApplicationService.updateCycle(id, cycleDto);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteCycle(UUID id) {
        cycleApplicationService.deleteCycleById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
