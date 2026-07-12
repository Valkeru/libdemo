package ru.valkeru.libdemo.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.valkeru.libdemo.constants.Database;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.time.Instant;

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

    @Version
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "version", nullable = false, columnDefinition = "bigint not null default 0")
    private long version;

    @CreationTimestamp
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz not null default now()")
    private Instant createdAt;

    @UpdateTimestamp
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "updated_at", insertable = false, columnDefinition = "timestamptz")
    private Instant updatedAt;

    public Role getRole() {
        return id.getRole();
    }

    public Permission getPermission() {
        return id.getPermission();
    }
}
