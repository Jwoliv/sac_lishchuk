package com.sac_lishchuk.repository;

import com.sac_lishchuk.enums.Rule;
import com.sac_lishchuk.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.file.Path;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    @Query("""
        select count(f) > 0 from File f
        join User u on u.email = :email and u.password = :password
        join Permission p on p.file.id = f.id
        where p.role = u.role and :rule member of p.rules and f.fileName = :fileName
    """)
    boolean checkPermissionOnFile(String fileName, String email, String password, Rule rule);

    Optional<File> findByFileName(String fileName);
}
