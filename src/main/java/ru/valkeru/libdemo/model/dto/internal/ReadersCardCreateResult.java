package ru.valkeru.libdemo.model.dto.internal;

import java.util.UUID;

public record ReadersCardCreateResult(UUID cardId, TokenDto token) {
}
