package com.sac_lishchuk.repository;

import com.sac_lishchuk.enums.MandatoryLevel;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("update User as u set u.password = :password, u.isComplexPassword = :isComplexPassword, u.expireDate = :expireDate where u.email = :email")
    void updatePasswordById(@Param("password") String password,
                            @Param("expireDate") LocalDate expireDate,
                            @Param("isComplexPassword") Boolean isComplexPassword,
                            @Param("email") String email);

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

    @Query("select u from User as u where u.email = :email and u.password = :password")
    Optional<User> findUserByEmailAndPassword(String email, String password);

    @Modifying
    @Query("update User as u set u.mandatoryLevel = :mandatoryLevel where u.email = :email")
    void updateMandatoryLevelByEmail(MandatoryLevel mandatoryLevel, String email);

    List<User> findByRole(Role role);
}
