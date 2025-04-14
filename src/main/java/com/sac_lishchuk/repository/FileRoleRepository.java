package com.sac_lishchuk.repository;

import com.sac_lishchuk.enums.Rule;
import com.sac_lishchuk.model.role.RoleFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRoleRepository extends JpaRepository<RoleFile, Long> {
    @Query("""
        select p.ipAddress from RoleFile f
        join User u on u.email = :email and u.password = :password
        join Permission p on p.roleFile.id = f.id
        where p.role = u.role and :rule member of p.rules and f.fileName = :fileName
    """)
    List<String> checkPermissionOnFile(String fileName, String email, String password, Rule rule);

    Optional<RoleFile> findByFileName(String fileName);
}
