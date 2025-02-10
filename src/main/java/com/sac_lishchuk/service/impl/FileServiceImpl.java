package com.sac_lishchuk.service.impl;

import com.sac_lishchuk.config.BusinessOptions;
import com.sac_lishchuk.config.exception.inner.NotAllowActionToCreatePermissionException;
import com.sac_lishchuk.config.exception.inner.NotAllowActionToFileException;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
import com.sac_lishchuk.model.File;
import com.sac_lishchuk.model.Permission;
import com.sac_lishchuk.model.User;
import com.sac_lishchuk.repository.FileRepository;
import com.sac_lishchuk.repository.PermissionRepository;
import com.sac_lishchuk.repository.UserRepository;
import com.sac_lishchuk.service.FileService;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.RegisterFileRequest;
import com.sac_lishchuk.shared.request.UserConfig;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.FileRegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final Rule READ_RULE = Rule.R;
    private static final Rule WRITE_RULE = Rule.W;

    private final FileRepository fileRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final BusinessOptions businessOptions;

    @Override
    @SneakyThrows
    @Transactional
    public FileRegisterResponse register(RegisterFileRequest request) {
        String fileName = request.getFileName();
        Path filePath = Path.of("files", fileName);
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        UserConfig adminConfig = request.getAdminConfig();
        Optional<User> admin = userRepository.checkUserByAllowRules(adminConfig.getEmail(), adminConfig.getPassword());
        if (admin.isPresent()) {
            User user = admin.get();
            var role = user.getRole();
            var allowChangeRole = businessOptions.getRoleCreatePermitList().get(role);
            var filteredPermissions = request.getPermissions().entrySet().stream()
                    .filter(r -> allowChangeRole.contains(r.getKey()))
                    .toList();
            File file = File.builder().fileName(fileName).build();
            fileRepository.save(file);
            List<Permission> permissions = filteredPermissions.stream().map(rtr -> build(rtr, file)).toList();
            permissionRepository.saveAll(permissions);
            return FileRegisterResponse.builder()
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
        if (fileRepository.checkPermissionOnFile(fileName, email, userConfig.getPassword(), READ_RULE)) {
            Path filePath = Path.of("files", fileName);
            if (!Files.exists(filePath)) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            return FileContentResponse.builder()
                    .fileName(fileName)
                    .content(content)
                    .build();
        }
        throw new NotAllowActionToFileException(email, READ_RULE, fileName);
    }

    @SneakyThrows
    public FileContentResponse write(FileContentActionRequest request) {
        UserConfig userConfig = request.getUserConfig();
        String fileName = request.getFileName();
        String email = userConfig.getEmail();
        if (fileRepository.checkPermissionOnFile(fileName, email, userConfig.getPassword(), WRITE_RULE)) {
            Path filePath = Path.of("files", fileName);
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, request.getNewContent(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            return FileContentResponse.builder()
                    .fileName(fileName)
                    .content(request.getNewContent())
                    .build();
        }
        throw new NotAllowActionToFileException(email, WRITE_RULE, fileName);
    }

    private Permission build(Map.Entry<Role, List<Rule>> rtr, File file) {
        return Permission.builder()
                .file(file)
                .rules(rtr.getValue())
                .role(rtr.getKey())
                .build();
    }

}
