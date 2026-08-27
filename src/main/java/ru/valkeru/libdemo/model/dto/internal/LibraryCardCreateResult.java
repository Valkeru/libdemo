package ru.valkeru.libdemo.model.dto.internal;

import java.util.UUID;

public record LibraryCardCreateResult(UUID cardId, TokenDto token) {
}
