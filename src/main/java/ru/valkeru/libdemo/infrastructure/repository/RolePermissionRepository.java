package ru.valkeru.libdemo.infrastructure.repository;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.infrastructure.entity.RolePermission;
import ru.valkeru.libdemo.infrastructure.entity.RolePermissionPk;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.util.Collection;

public interface RolePermissionRepository extends BaseJpaRepository<RolePermission, RolePermissionPk> {

    Collection<RolePermission> findAllByIdRole(Role role);

    @Modifying
    @Query("delete from RolePermission rp where rp.id.role = :role")
    void deletePermissions(Role role);

    boolean existsByIdRoleAndIdPermission(Role idRole, Permission idPermission);
}
