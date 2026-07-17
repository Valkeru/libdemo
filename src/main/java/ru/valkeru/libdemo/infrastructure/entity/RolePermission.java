package ru.valkeru.libdemo.infrastructure.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = Database.SCHEMA_SECURITY, name = "role_permission")
public class RolePermission {

    @EmbeddedId
    @Setter(AccessLevel.NONE)
    private RolePermissionPk id;

    public Role getRole() {
        return id.getRole();
    }

    public Permission getPermission() {
        return id.getPermission();
    }
}
