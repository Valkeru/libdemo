package ru.valkeru.libdemo.infrastructure.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.EnumJdbcType;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class RolePermissionPk implements Serializable {

    @Serial
    private static final long serialVersionUID = -4064024468725355674L;

    @Enumerated(EnumType.STRING)
    @JdbcType(EnumJdbcType.class)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Permission permission;
}
