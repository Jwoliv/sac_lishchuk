package com.sac_lishchuk.repository;

import com.sac_lishchuk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("update User as u set u.password = :password, u.isComplexPassword = :isComplexPassword where u.id = :userId")
    void updatePasswordById(@Param("password") String password,
                            @Param("isComplexPassword") Boolean isComplexPassword,
                            @Param("userId") Long userId);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User as u set u.isLogged = true where u.email = :email and u.password = :password")
    void login(String email, String password);

    @Modifying
    @Query("update User as u set u.isLogged = false where u.email = :email")
    void logout(String email);

    @Query("select u.isLogged from User as u where u.email = :email")
    Boolean getLoggedStatus(String email);

    List<User> getAllByIsLogged(boolean isLogged);
}
