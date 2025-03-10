package com.sac_lishchuk.repository;

import com.sac_lishchuk.model.User;
import com.sac_lishchuk.model.discretionary.DiscretionaryFile;
import com.sac_lishchuk.model.discretionary.DiscretionaryMatrix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscretionaryMatrixRepository extends JpaRepository<DiscretionaryMatrix, Long> {
    Optional<DiscretionaryMatrix> findByUserAndFile(User user, DiscretionaryFile file);
}
