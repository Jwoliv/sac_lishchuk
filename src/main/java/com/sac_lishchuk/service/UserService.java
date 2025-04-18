package com.sac_lishchuk.service;

import com.sac_lishchuk.shared.dto.CreateUserRequest;
import com.sac_lishchuk.shared.dto.UserDto;
import com.sac_lishchuk.shared.request.*;
import com.sac_lishchuk.shared.response.SuccessChangedPasswordResponse;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto createUser(CreateUserRequest request);
    SuccessChangedPasswordResponse changePassword(ChangePasswordRequest request);
    boolean login(LoginRequest request);
    void logout(LogoutRequest request);
    List<UserDto> getAllLogged();
    List<UserDto> findUsersByRequest(FindUserRequest request);
    ChangeMandatoryPermissionResponse changeMandatoryPermission(ChangeMandatoryPermissionRequest request);
}
