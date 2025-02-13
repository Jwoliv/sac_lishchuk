package com.sac_lishchuk.repository;

import com.sac_lishchuk.model.mandatory.MandatoryFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMandatoryRepository extends JpaRepository<MandatoryFile, Long> {
}
