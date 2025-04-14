package com.sac_lishchuk.service.impl;

import com.sac_lishchuk.config.BusinessOptions;
import com.sac_lishchuk.config.exception.inner.*;
import com.sac_lishchuk.enums.MandatoryLevel;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.mapper.UserMapper;
import com.sac_lishchuk.model.User;
import com.sac_lishchuk.repository.UserRepository;
import com.sac_lishchuk.service.UserService;
import com.sac_lishchuk.shared.dto.CreateUserRequest;
import com.sac_lishchuk.shared.dto.UserDto;
import com.sac_lishchuk.shared.request.*;
import com.sac_lishchuk.shared.response.SuccessChangedPasswordResponse;
import com.sac_lishchuk.utils.PasswordChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.sac_lishchuk.enums.Role.ADMIN;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordChecker passwordChecker;
    private final BusinessOptions businessOptions;
    @PersistenceContext
    private final EntityManager entityManager;

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
        if (userRepository.findByRole(ADMIN).size() >= 2) {
            throw new CanNotCreateAdminException();
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserHasAlreadyExistException(request.getEmail());
        }
        var user = userMapper.mapToCreateUser(request);
        if (passwordChecker.isValidPasswordComplexity(request.getPassword(), request.getIsComplexPassword())) {
            if (!Objects.equals(request.getRole(), Role.USER)) {
                var adminConfig = request.getAdminConfig();
                if (Objects.nonNull(adminConfig)) {
                    Optional<User> optAdmin = userRepository.findUserByEmailAndPassword(adminConfig.getEmail(), adminConfig.getPassword());
                    if (optAdmin.isEmpty()) {
                        throw new NotAllowActionToCreateUserException();
                    } else {
                        user.setMandatoryLevel(Optional.ofNullable(request.getMandatoryLevel()).orElse(MandatoryLevel.PUBLIC));
                        if (List.of(MandatoryLevel.TOP_SECRET, MandatoryLevel.SECRET).contains(user.getMandatoryLevel())) {
                            if (!passwordChecker.isValidPasswordComplexity(request.getPassword(), true)) {
                                throw new InvalidPasswordException(request.getPassword());
                            }
                        }
                    }
                } else {
                    throw new NotAllowActionToCreateUserException();
                }
            }
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

    @Override
    public List<UserDto> findUsersByRequest(FindUserRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserDto> query = cb.createQuery(UserDto.class);
        Root<User> root = query.from(User.class);
        List<Predicate> predicates = buildPredicates(cb, root, request);
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        query.select(cb.construct(UserDto.class,
                root.get("id"),
                root.get("firstName"),
                root.get("lastName"),
                root.get("middleName"),
                root.get("email"),
                root.get("password")
        ));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @Transactional
    public ChangeMandatoryPermissionResponse changeMandatoryPermission(ChangeMandatoryPermissionRequest request) {
        String email = request.getEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            UserConfig adminConfig = request.getAdminConfig();
            Optional<User> adminOpt = userRepository.findUserByEmailAndPassword(adminConfig.getEmail(), adminConfig.getPassword());
            if (adminOpt.isPresent() && adminOpt.get().getRole().equals(ADMIN)) {
                MandatoryLevel mandatoryLevel = request.getMandatoryLevel();
                userRepository.updateMandatoryLevelByEmail(mandatoryLevel, email);
                return ChangeMandatoryPermissionResponse.builder().email(email).mandatoryLevel(mandatoryLevel).build();
            }
            throw new NotAllowActionToChangeMandatoryPermissionException(adminConfig.getEmail());
        }
        throw new NotFoundElementException(User.class, email);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<User> root, FindUserRequest request) {
        return Map.of("firstName", request.getFirstName(), "lastName", request.getLastName(), "email", request.getEmail())
                .entrySet()
                .stream()
                .filter(entry -> Objects.nonNull(entry.getValue()) && !entry.getValue().isBlank())
                .map(entry -> cb.like(root.get(entry.getKey()), "%%%s%%".formatted(entry.getValue())))
                .toList();
    }


    private boolean checkPermitMap(CreateUserRequest request, Optional<User> optAdmin) {
        var admin = optAdmin.orElseThrow();
        return businessOptions.getRoleCreatePermitList()
                .get(admin.getRole())
                .contains(request.getRole());
    }

}
