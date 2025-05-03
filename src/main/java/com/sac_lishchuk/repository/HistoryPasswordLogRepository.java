package com.sac_lishchuk.repository;

import com.sac_lishchuk.model.HistoryPasswordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryPasswordLogRepository extends JpaRepository<HistoryPasswordLog, Long> {
    @Query("""
        SELECT HPL.password FROM HistoryPasswordLog AS HPL
        WHERE HPL.email = :email
        ORDER BY HPL.loggedAt DESC
        LIMIT 3
    """)
    List<String> getThreeLastPasswordsByEmail(String email);
}
