package ru.valkeru.libdemo.model.dto.security;

import ru.valkeru.libdemo.security.Role;

import java.util.UUID;

public record TokenPayload(UUID userId, String subject,Role role) {
}
