package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.application.LibraryCardApplicationService;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;
import ru.valkeru.libdemo.web.api.service.LibraryCardServiceApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class LibraryCardServiceController implements LibraryCardServiceApi {

    private final LibraryCardApplicationService service;

    @Override
    public ResponseEntity<Void> createLibraryCardStaff(LibraryCardCreateDto dto) {
        UUID cardId = service.createLibraryCard(dto);

        return ResponseEntity.created(
                MvcUriComponentsBuilder
                    .fromMethodCall(on(LibraryCardServiceApi.class).getLibraryCard(cardId))
                    .build().toUri()
            )
            .build();
    }

    @Override
    public ResponseEntity<Object> getLibraryCard(UUID id) {
        return null;
    }
}
