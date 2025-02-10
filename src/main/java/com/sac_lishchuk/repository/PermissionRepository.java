package com.sac_lishchuk.repository;

import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
import com.sac_lishchuk.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByFileIdAndRole(Long id, Role key);
}
