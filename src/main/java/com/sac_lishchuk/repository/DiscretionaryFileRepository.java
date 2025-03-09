package com.sac_lishchuk.repository;

import com.sac_lishchuk.model.discretionary.DiscretionaryFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscretionaryFileRepository extends JpaRepository<DiscretionaryFile, Long> {
    Optional<DiscretionaryFile> findByFileName(String fileName);
}
