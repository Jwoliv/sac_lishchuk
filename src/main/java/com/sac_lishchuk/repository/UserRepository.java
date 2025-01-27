package com.sac_lishchuk.repository;

import com.sac_lishchuk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("update User as u set u.password = :password, u.isComplexPassword = :isComplexPassword where u.id = :userId")
    void updatePasswordById(@Param("password") String password,
                            @Param("isComplexPassword") Boolean isComplexPassword,
                            @Param("userId") Long userId);

    Optional<User> findByEmail(String email);
}
