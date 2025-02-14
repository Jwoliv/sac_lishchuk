package com.sac_lishchuk.repository;

import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.model.role.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByRoleFileIdAndRole(Long id, Role key);
}
