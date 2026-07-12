package ru.valkeru.libdemo.infrastructure.repository;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import ru.valkeru.libdemo.infrastructure.entity.RolePermission;
import ru.valkeru.libdemo.infrastructure.entity.RolePermissionPk;
import ru.valkeru.libdemo.security.Role;

import java.util.Collection;

public interface RolePermissionRepository extends BaseJpaRepository<RolePermission, RolePermissionPk> {

    Collection<RolePermission> findAllByIdRole(Role role);
}
