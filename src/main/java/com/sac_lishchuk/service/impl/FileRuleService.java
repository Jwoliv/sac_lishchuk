package com.sac_lishchuk.service.impl;

import com.sac_lishchuk.config.BusinessOptions;
import com.sac_lishchuk.config.exception.inner.NotAllowActionToCreatePermissionException;
import com.sac_lishchuk.config.exception.inner.NotAllowActionToFileException;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
import com.sac_lishchuk.model.User;
import com.sac_lishchuk.model.role.RoleFile;
import com.sac_lishchuk.model.role.Permission;
import com.sac_lishchuk.repository.FileRoleRepository;
import com.sac_lishchuk.repository.PermissionRepository;
import com.sac_lishchuk.repository.UserRepository;
import com.sac_lishchuk.service.FileRuleServiceI;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.UserConfig;
import com.sac_lishchuk.shared.request.role.ChangePermissionForFileRoleRequest;
import com.sac_lishchuk.shared.request.role.RegisterFileRoleRequest;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.role.ChangePermissionRoleResponse;
import com.sac_lishchuk.shared.response.role.FileRoleRegisterResponse;
import com.sac_lishchuk.utils.FileActionExecutor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.sac_lishchuk.utils.FileActionExecutor.checkExistenceFile;

@Service
@RequiredArgsConstructor
public class FileRuleService implements FileRuleServiceI {
    private static final Rule READ_RULE = Rule.R;
    private static final Rule WRITE_RULE = Rule.W;
    private static final Rule EXECUTE_RULE = Rule.X;

    private final FileRoleRepository fileRoleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final BusinessOptions businessOptions;

    @Override
    @SneakyThrows
    @Transactional
    public FileRoleRegisterResponse register(RegisterFileRoleRequest request) {
        String fileName = checkExistenceFile(request.getFileName());
        UserConfig adminConfig = request.getAdminConfig();
        Optional<User> admin = userRepository.findUserByEmailAndPassword(adminConfig.getEmail(), adminConfig.getPassword());
        if (admin.isPresent()) {
            User user = admin.get();
            var role = user.getRole();
            var allowChangeRole = businessOptions.getRoleCreatePermitList().get(role);
            var filteredPermissions = request.getPermissions().entrySet().stream()
                    .filter(r -> allowChangeRole.contains(r.getKey()))
                    .toList();
            RoleFile roleFile = RoleFile.builder().fileName(fileName).build();
            fileRoleRepository.save(roleFile);
            List<Permission> permissions = filteredPermissions.stream().map(rtr -> build(rtr, roleFile)).toList();
            permissionRepository.saveAll(permissions);
            return FileRoleRegisterResponse.builder()
                    .fileName(fileName)
                    .permissions(request.getPermissions())
                    .occurAt(LocalDateTime.now())
                    .build();
        }
        throw new NotAllowActionToCreatePermissionException(fileName);
    }

    @Override
    @SneakyThrows
    public FileContentResponse read(FileContentActionRequest request) {
        UserConfig userConfig = request.getUserConfig();
        String fileName = request.getFileName();
        String email = userConfig.getEmail();
        if (fileRoleRepository.checkPermissionOnFile(fileName, email, userConfig.getPassword(), READ_RULE).contains(request.getIpAddress())) {
            return FileActionExecutor.read(fileName);
        }
        throw new NotAllowActionToFileException(email, READ_RULE, fileName);
    }

    @Override
    public ResponseEntity<InputStreamResource> readImage(FileContentActionRequest request) {
        UserConfig userConfig = request.getUserConfig();
        String fileName = request.getFileName();
        String email = userConfig.getEmail();
        if (fileRoleRepository.checkPermissionOnFile(fileName, email, userConfig.getPassword(), READ_RULE).contains(request.getIpAddress())) {
            return FileActionExecutor.readImg(request);
        }
        throw new NotAllowActionToFileException(email, READ_RULE, fileName);
    }

    @Override
    @SneakyThrows
    public FileContentResponse write(FileContentActionRequest request) {
        UserConfig userConfig = request.getUserConfig();
        String fileName = request.getFileName();
        String email = userConfig.getEmail();
        if (fileRoleRepository.checkPermissionOnFile(fileName, email, userConfig.getPassword(), WRITE_RULE).contains(request.getIpAddress())) {
            return FileActionExecutor.write(request);
        }
        throw new NotAllowActionToFileException(email, WRITE_RULE, fileName);
    }

    @SneakyThrows
    public FileContentResponse execute(FileContentActionRequest request) {
        String fileName = checkExistenceFile(request.getFileName());
        if (fileRoleRepository.checkPermissionOnFile(fileName, request.getUserConfig().getEmail(), request.getUserConfig().getPassword(), EXECUTE_RULE).contains(request.getIpAddress())) {
            var response = FileActionExecutor.execute(request);
            response.setContent("файл виконався коректно");
            return response;
        }
        throw new NotAllowActionToFileException(request.getUserConfig().getEmail(), EXECUTE_RULE, fileName);
    }

    @Override
    @Transactional
    public ChangePermissionRoleResponse changePermission(ChangePermissionForFileRoleRequest request) {
        String fileName = checkExistenceFile(request.getFileName());
        UserConfig userConfig = request.getUserConfig();
        var user = userRepository.findUserByEmailAndPassword(userConfig.getEmail(), userConfig.getPassword());
        if (user.isPresent()) {
            var role = user.get().getRole();
            Map<Role, List<Role>> roleCreatePermitList = businessOptions.getRoleCreatePermitList();
            Set<Role> notInheritanceRoles = request.getPermissions()
                    .keySet()
                    .stream()
                    .filter(rr -> !roleCreatePermitList.get(role).contains(rr))
                    .collect(Collectors.toSet());
            if (!notInheritanceRoles.isEmpty()) {
                throw new NotAllowActionToFileException(request.getUserConfig().getEmail(), "зміну правил", fileName, notInheritanceRoles);
            }
            Optional<RoleFile> optFile = fileRoleRepository.findByFileName(fileName);
            optFile.ifPresent(roleFile -> {
                switch (request.getAction()) {
                    case ADD -> {
                        Map<Role, List<Rule>> rules = request.getPermissions();
                        rules.entrySet().forEach(valueRules -> {
                            if (permissionRepository.findByRoleFileIdAndRole(roleFile.getId(), valueRules.getKey()).isEmpty()) {
                                Permission permissions = build(valueRules, roleFile);
                                permissionRepository.save(permissions);
                            } else {
                                Permission permission = permissionRepository.findByRoleFileIdAndRole(roleFile.getId(), valueRules.getKey()).orElseThrow();
                                valueRules.getValue().forEach(rule -> permission.getRules().add(rule));
                                permissionRepository.save(permission);
                            }
                        });
                    }
                    case REMOVE -> {
                        Map<Role, List<Rule>> rules = request.getPermissions();
                        rules.forEach((key, value) -> {
                            Permission permission = permissionRepository.findByRoleFileIdAndRole(roleFile.getId(), key).orElseThrow();
                            value.forEach(rule -> permission.getRules().remove(rule));
                            permissionRepository.save(permission);
                        });
                    }
                }
            });
            return ChangePermissionRoleResponse.builder()
                    .fileName(fileName)
                    .changedPermissions(request.getPermissions())
                    .build();
        }
        throw new NotAllowActionToFileException(request.getUserConfig().getEmail(), "зміну правил", fileName);
    }

    private Permission build(Map.Entry<Role, List<Rule>> rtr, RoleFile roleFile) {
        return Permission.builder()
                .roleFile(roleFile)
                .rules(new HashSet<>(rtr.getValue()))
                .role(rtr.getKey())
                .build();
    }

}
