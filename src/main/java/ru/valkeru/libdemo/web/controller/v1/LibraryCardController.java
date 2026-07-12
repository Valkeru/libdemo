package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;
import ru.valkeru.libdemo.model.dto.internal.LibraryCardCreateResult;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.application.LibraryCardApplicationService;
import ru.valkeru.libdemo.web.api.service.LibraryCardApi;
import ru.valkeru.libdemo.web.api.service.LibraryCardServiceApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class LibraryCardController implements LibraryCardApi, LibraryCardServiceApi {

    private final LibraryCardApplicationService service;

    @Override
    public ResponseEntity<Void> createLibraryCard(LibraryCardCreateDto dto) {
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

    @Override
    public ResponseEntity<Void> createLibraryCard(LibraryPrincipal principal) {
        LibraryCardCreateResult result = service.createLibraryCard(principal);

        ResponseEntity.BodyBuilder builder = ResponseEntity.created(
                MvcUriComponentsBuilder.fromMethodCall(
                    on(LibraryCardApi.class).getLibraryCard(result.cardId(), principal)
                ).build().toUri()
            );

        TokenDto token = result.token();
        if (token != null) {
            builder
                .header(CustomHeaders.ACCESS_TOKEN, token.getAccessToken())
                .header(CustomHeaders.REFRESH_TOKEN, token.getRefreshToken());
        }

        return builder.build();
    }

    @Override
    public ResponseEntity<Object> getCurrentLibraryCard(LibraryPrincipal principal) {
        return null;
    }

    @Override
    public ResponseEntity<Object> getLibraryCard(UUID id, LibraryPrincipal principal) {
        return null;
    }
}
