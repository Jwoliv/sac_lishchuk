package com.sac_lishchuk.service.impl;

import com.sac_lishchuk.config.exception.inner.NotAllowActionToFileException;
import com.sac_lishchuk.config.exception.inner.NotFoundElementException;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
import com.sac_lishchuk.model.User;
import com.sac_lishchuk.model.discretionary.DiscretionaryFile;
import com.sac_lishchuk.model.discretionary.DiscretionaryMatrix;
import com.sac_lishchuk.repository.DiscretionaryFileRepository;
import com.sac_lishchuk.repository.DiscretionaryMatrixRepository;
import com.sac_lishchuk.repository.UserRepository;
import com.sac_lishchuk.service.FileDiscretionaryServiceI;
import com.sac_lishchuk.shared.request.ChangePermissionDiscretionaryRequest;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.RegisterFileDiscretionaryRequest;
import com.sac_lishchuk.shared.request.UserConfig;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.utils.FileActionExecutor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.sac_lishchuk.enums.Rule.*;

@Service
public class FileDiscretionaryService implements FileDiscretionaryServiceI {
    @Setter(onMethod = @__({@Autowired}))
    private DiscretionaryMatrixRepository discretionaryMatrixRepository;
    @Setter(onMethod = @__({@Autowired}))
    private DiscretionaryFileRepository discretionaryFileRepository;
    @Setter(onMethod = @__({@Autowired}))
    private UserRepository userRepository;

    @Override
    public FileContentResponse read(FileContentActionRequest request) {
        checkPermissions(request, R);
        return FileActionExecutor.read(request.getFileName());
    }

    @Override
    public ResponseEntity<InputStreamResource> readImage(FileContentActionRequest request) {
        checkPermissions(request, R);
        return FileActionExecutor.readImg(request);
    }

    @Override
    public FileContentResponse write(FileContentActionRequest request) {
        checkPermissions(request, W);
        return FileActionExecutor.write(request);
    }

    @Override
    public FileContentResponse execute(FileContentActionRequest request) {
        checkPermissions(request, X);
        return FileActionExecutor.execute(request);
    }

    @Override
    public DiscretionaryMatrix register(RegisterFileDiscretionaryRequest request) {
        DiscretionaryFile file = DiscretionaryFile.builder().fileName(request.getFileName()).build();
        User user = userRepository.findUserByEmailAndPassword(request.getAdminConfig().getEmail(), request.getAdminConfig().getPassword()).orElseThrow();
        DiscretionaryFile savedFile = discretionaryFileRepository.save(file);
        DiscretionaryMatrix matrix = DiscretionaryMatrix.builder()
                .user(user)
                .file(savedFile)
                .executable(true)
                .writeable(true)
                .readable(true)
                .sinceAccess(LocalDateTime.now())
                .toAccess(LocalDateTime.of(2026, 1, 1, 0, 0,0 ))
                .build();
        discretionaryMatrixRepository.save(matrix);
        return matrix;
    }

    @Override
    public DiscretionaryMatrix changePermission(ChangePermissionDiscretionaryRequest request) {
        ChangePermissionDiscretionaryRequest.ChangePermissionAction action = request.getAction();
        UserConfig adminConfig = request.getAdminConfig();
        User admin = userRepository.findUserByEmailAndPassword(adminConfig.getEmail(), adminConfig.getPassword()).orElseThrow();
        if (admin.getRole().equals(Role.ADMIN)) {
            throw new NotFoundElementException(User.class, adminConfig.getEmail());
        }
        User user = userRepository.findByEmail(request.getUserRuleToFile().getEmail()).orElseThrow();
        DiscretionaryFile file = discretionaryFileRepository.findByFileName(request.getFileName()).orElseThrow();
        DiscretionaryMatrix matrix = discretionaryMatrixRepository.findByUserAndFile(user, file);
        switch (action) {
            case ADD -> processChangePermissions(request, matrix, true);
            case REMOVE -> processChangePermissions(request, matrix, false);
        }
        return discretionaryMatrixRepository.save(matrix);
    }

    private void processChangePermissions(ChangePermissionDiscretionaryRequest request, DiscretionaryMatrix matrix, boolean access) {
        request.getUserRuleToFile().getRules().forEach(rule -> {
            switch (rule) {
                case R -> matrix.setReadable(access);
                case W -> matrix.setWriteable(access);
                case X -> matrix.setExecutable(access);
            }
            if (access) {
                matrix.setSinceAccess(request.getSinceAccess());
                matrix.setToAccess(request.getToAccess());
            } else {
                matrix.setSinceAccess(null);
                matrix.setToAccess(null);
            }
        });
    }

    private void checkPermissions(FileContentActionRequest request, Rule rule) {
        DiscretionaryFile file = discretionaryFileRepository.findByFileName(request.getFileName()).orElseThrow();
        UserConfig userCOnfig = request.getUserConfig();
        User user = userRepository.findUserByEmailAndPassword(userCOnfig.getEmail(), userCOnfig.getPassword()).orElseThrow();
        DiscretionaryMatrix matrix = discretionaryMatrixRepository.findByUserAndFile(user, file);
        Boolean isAccess = switch (rule) {
            case R -> matrix.getReadable();
            case W -> matrix.getWriteable();
            case X -> matrix.getExecutable();
        };
        LocalDateTime now = LocalDateTime.now();
        if (!isAccess || !now.isAfter(matrix.getSinceAccess()) || !now.isBefore(matrix.getToAccess())) {
            throw new NotAllowActionToFileException(request.getUserConfig().getEmail(), rule, file.getFileName());
        }
    }
}
