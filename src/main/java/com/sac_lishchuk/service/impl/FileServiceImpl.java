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
import com.sac_lishchuk.shared.exception.UnknownFileException;
import com.sac_lishchuk.shared.request.ChangePermissionRequest;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.RegisterFileRequest;
import com.sac_lishchuk.shared.request.UserConfig;
import com.sac_lishchuk.shared.response.ChangePermissionResponse;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.FileRegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final Rule READ_RULE = Rule.R;
    private static final Rule WRITE_RULE = Rule.W;
    private static final Rule EXECUTE_RULE = Rule.X;

    private final FileRepository fileRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final BusinessOptions businessOptions;

    @Override
    @SneakyThrows
    @Transactional
    public FileRegisterResponse register(RegisterFileRequest request) {
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

    @Override
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

    @SneakyThrows
    public FileContentResponse execute(FileContentActionRequest request) {
        String fileName = checkExistenceFile(request.getFileName());
        if (fileRepository.checkPermissionOnFile(fileName, request.getUserConfig().getEmail(), request.getUserConfig().getPassword(), EXECUTE_RULE)) {
            ProcessBuilder processBuilder = new ProcessBuilder("wsl", "bash", "files/" + fileName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                 String line;
                 while ((line = reader.readLine()) != null) {
                     System.out.println("Output: " + line);
                 }
                 String errorLine;
                 while ((errorLine = errorReader.readLine()) != null) {
                     System.out.println("Error: " + errorLine);
                 }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Bash script execution failed with exit code: " + exitCode);
            }
            return FileContentResponse.builder().fileName(fileName).build();
        }
        throw new NotAllowActionToFileException(request.getUserConfig().getEmail(), EXECUTE_RULE, fileName);
    }

    @Override
    @Transactional
    public ChangePermissionResponse changePermission(ChangePermissionRequest request) {
        String fileName = checkExistenceFile(request.getFileName());
        UserConfig userConfig = request.getUserConfig();
        var admin = userRepository.findUserByEmailAndPassword(userConfig.getEmail(), userConfig.getPassword());
        if (admin.isPresent()) {
            Optional<File> optFile = fileRepository.findByFileName(fileName);
            optFile.ifPresent(file -> {
                switch (request.getAction()) {
                    case ADD -> {
                        Map<Role, List<Rule>> rules = request.getPermissions();
                        rules.entrySet().forEach(valueRules -> {
                            if (permissionRepository.findByFileIdAndRole(file.getId(), valueRules.getKey()).isEmpty()) {
                                Permission permissions = build(valueRules, file);
                                permissionRepository.save(permissions);
                            } else {
                                Permission permission = permissionRepository.findByFileIdAndRole(file.getId(), valueRules.getKey()).orElseThrow();
                                valueRules.getValue().forEach(rule -> permission.getRules().add(rule));
                            }
                        });

                    }
                    case REMOVE -> request.getPermissions().forEach((role, rules) -> {
                        // todo
                    });
                }
            });
            return ChangePermissionResponse.builder()
                    .fileName(fileName)
                    .changedPermissions(request.getPermissions())
                    .build();
        }
        throw new NotAllowActionToFileException(request.getUserConfig().getEmail(), "зміну правил", fileName);
    }

    private String checkExistenceFile(String fileName) {
        Path filePath = Path.of("files/%s".formatted(fileName));
        if (!Files.exists(filePath)) {
            throw new UnknownFileException(fileName);
        }
        return fileName;
    }


    private Permission build(Map.Entry<Role, List<Rule>> rtr, File file) {
        return Permission.builder()
                .file(file)
                .rules(new HashSet<>(rtr.getValue()))
                .role(rtr.getKey())
                .build();
    }

}
