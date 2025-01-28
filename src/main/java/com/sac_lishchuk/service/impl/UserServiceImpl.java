package com.sac_lishchuk.service.impl;

import com.sac_lishchuk.config.exception.inner.UserHasAlreadyExistException;
import com.sac_lishchuk.config.exception.inner.InvalidPasswordException;
import com.sac_lishchuk.config.exception.inner.NotFoundElementException;
import com.sac_lishchuk.mapper.UserMapper;
import com.sac_lishchuk.model.User;
import com.sac_lishchuk.repository.UserRepository;
import com.sac_lishchuk.service.UserService;
import com.sac_lishchuk.shared.request.LoginRequest;
import com.sac_lishchuk.shared.request.LogoutRequest;
import com.sac_lishchuk.utils.PasswordChecker;
import com.sac_lishchuk.shared.dto.UserDto;
import com.sac_lishchuk.shared.request.ChangePasswordRequest;
import com.sac_lishchuk.shared.request.CreateUserRequest;
import com.sac_lishchuk.shared.response.SuccessChangedPasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordChecker passwordChecker;

    @Override
    public List<UserDto> getUsers() {
        var users = userRepository.findAll();
        return userMapper.mapEntityToDto(users);
    }

    @Override
    public UserDto getUserById(Long id) {
        var userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return userMapper.mapEntityToDto(userOpt.get());
        }
        throw new NotFoundElementException(User.class, id);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return userMapper.mapEntityToDto(userOpt.get());
        }
        throw new NotFoundElementException(User.class, email);
    }

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserHasAlreadyExistException(request.getEmail());
        }
        var user = userMapper.mapToCreateUser(request);
        if (passwordChecker.isValidPasswordComplexity(request.getPassword(), request.getIsComplexPassword())) {
            var savedUser = userRepository.save(user);
            return userMapper.mapEntityToDto(savedUser);
        }
        throw new InvalidPasswordException(null, request.getPassword());
    }

    @Override
    @Transactional
    public SuccessChangedPasswordResponse changePassword(ChangePasswordRequest request) {
        Long userId = request.getUserId();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            var password = request.getPassword();
            var isComplexPassword = request.getIsComplexPassword();
            if (passwordChecker.isValidPasswordComplexity(password, isComplexPassword)) {
                userRepository.updatePasswordById(password, isComplexPassword, userId);
                return userMapper.mapToSuccessChangePasswordResponse(user, request);
            }
            throw new InvalidPasswordException(user.getId(), request.getPassword());
        }
        throw new NotFoundElementException(User.class, userId);
    }

    @Override
    @Transactional
    public boolean login(LoginRequest request) {
        String email = request.getEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            if (!Objects.equals(userOpt.get().getPassword(), request.getPassword())) {
                userRepository.logout(email);
                return false;
            }
            userRepository.login(email, request.getPassword());
            return userRepository.getLoggedStatus(email);
        }
        throw new NotFoundElementException(User.class, email);
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        String email = request.getEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            userRepository.logout(email);
            return;
        }
        throw new NotFoundElementException(User.class, email);
    }

    @Override
    public List<UserDto> getAllLogged() {
        var users = userRepository.getAllByIsLogged(true);
        return userMapper.mapEntityToDto(users);
    }

}
