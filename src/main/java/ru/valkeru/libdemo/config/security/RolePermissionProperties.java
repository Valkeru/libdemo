package ru.valkeru.libdemo.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.util.Collection;
import java.util.Map;

@ConfigurationProperties(prefix = "app.security")
@RequiredArgsConstructor
public class RolePermissionProperties {

    @Getter
    private final Map<Role, Collection<Permission>> permissions;
}
