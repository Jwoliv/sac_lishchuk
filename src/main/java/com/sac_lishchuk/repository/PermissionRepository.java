package com.sac_lishchuk.repository;

import com.sac_lishchuk.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
