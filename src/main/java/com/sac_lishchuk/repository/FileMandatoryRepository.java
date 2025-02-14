package com.sac_lishchuk.repository;

import com.sac_lishchuk.enums.MandatoryLevel;
import com.sac_lishchuk.model.mandatory.MandatoryFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileMandatoryRepository extends JpaRepository<MandatoryFile, Long> {
    Optional<MandatoryFile> findByFileName(String fileName);

    @Modifying
    @Query("update MandatoryFile mf set mf.mandatoryLevel = :mandatoryLevel where mf.fileName = :fileName")
    void updateMandatoryLevelByFileName(@Param("mandatoryLevel") MandatoryLevel mandatoryLevel,
                                        @Param("fileName") String fileName);

}
