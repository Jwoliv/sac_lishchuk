package com.sac_lishchuk.service.impl;

import com.sac_lishchuk.config.exception.inner.NotAllowActionToCreatePermissionException;
import com.sac_lishchuk.config.exception.inner.NotAllowActionToFileException;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.model.User;
import com.sac_lishchuk.model.mandatory.MandatoryFile;
import com.sac_lishchuk.repository.FileMandatoryRepository;
import com.sac_lishchuk.repository.UserRepository;
import com.sac_lishchuk.service.FileMandatoryServiceI;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.UserConfig;
import com.sac_lishchuk.shared.request.mandatory.ChangePermissionForFileMandatoryRequest;
import com.sac_lishchuk.shared.request.mandatory.RegisterFileMandatoryRequest;
import com.sac_lishchuk.shared.response.mandatory.ChangePermissionMandatoryResponse;
import com.sac_lishchuk.shared.response.mandatory.FileMandatoryRegisterResponse;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.utils.FileActionExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sac_lishchuk.utils.FileActionExecutor.checkExistenceFile;

@Service
@RequiredArgsConstructor
public class FileMandatoryService implements FileMandatoryServiceI {
    private final FileMandatoryRepository fileMandatoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FileMandatoryRegisterResponse register(RegisterFileMandatoryRequest request) {
        String fileName = checkExistenceFile(request.getFileName());
        UserConfig adminConfig = request.getAdminConfig();
        Optional<User> adminOpt = userRepository.findUserByEmailAndPassword(adminConfig.getEmail(), adminConfig.getPassword());
        if (adminOpt.isPresent() && adminOpt.get().getRole().equals(Role.ADMIN)) {
            MandatoryFile mandatoryFile = MandatoryFile.builder()
                    .fileName(fileName)
                    .mandatoryLevel(request.getMandatoryLevel())
                    .build();
            fileMandatoryRepository.save(mandatoryFile);
        }
        throw new NotAllowActionToCreatePermissionException(fileName);
    }

    @Override
    @Transactional
    public ChangePermissionMandatoryResponse changePermission(ChangePermissionForFileMandatoryRequest request) {
        String fileName = checkExistenceFile(request.getFileName());
        UserConfig adminConfig = request.getUserConfig();
        Optional<User> adminOpt = userRepository.findUserByEmailAndPassword(adminConfig.getEmail(), adminConfig.getPassword());
        if (adminOpt.isPresent() && adminOpt.get().getRole().equals(Role.ADMIN) && fileMandatoryRepository.findByFileName(fileName).isPresent()) {
            fileMandatoryRepository.updateMandatoryLevelByFileName(request.getMandatoryLevel(), fileName);
        }
        throw new NotAllowActionToFileException(request.getUserConfig().getEmail(), "зміну правил", fileName);
    }

    @Override
    public FileContentResponse read(FileContentActionRequest request) {
        checkPermissions(request);
        return FileActionExecutor.read(request.getFileName());
    }

    @Override
    public FileContentResponse write(FileContentActionRequest request) {
        checkPermissions(request);
        return FileActionExecutor.write(request);
    }

    @Override
    public FileContentResponse execute(FileContentActionRequest request) {
        checkPermissions(request);
        return FileActionExecutor.execute(request);
    }

    public void checkPermissions(FileContentActionRequest request) {
        UserConfig userConfig = request.getUserConfig();
        var isAllow = userRepository.findUserByEmailAndPassword(userConfig.getEmail(), userConfig.getPassword())
                .flatMap(user -> fileMandatoryRepository.findByFileName(request.getFileName())
                        .map(file -> user.getMandatoryLevel().compareTo(file.getMandatoryLevel()) > 0))
                .orElse(false);
        if (!isAllow) {
            throw new NotAllowActionToFileException(userConfig.getEmail(), request.getFileName());
        }
    }

}
