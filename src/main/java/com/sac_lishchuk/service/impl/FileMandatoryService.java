package com.sac_lishchuk.service.impl;

import com.sac_lishchuk.repository.FileMandatoryRepository;
import com.sac_lishchuk.service.FileMandatoryServiceI;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.mandatory.RegisterFileMandatoryRequest;
import com.sac_lishchuk.shared.request.role.ChangePermissionForFileRoleRequest;
import com.sac_lishchuk.shared.response.mandatory.ChangePermissionMandatoryResponse;
import com.sac_lishchuk.shared.response.mandatory.FileMandatoryRegisterResponse;
import com.sac_lishchuk.shared.response.role.ChangePermissionRoleResponse;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.role.FileRoleRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileMandatoryService implements FileMandatoryServiceI {
    private final FileMandatoryRepository fileMandatoryRepository;

    @Override
    public FileMandatoryRegisterResponse register(RegisterFileMandatoryRequest request) {
        return null;
    }

    @Override
    public ChangePermissionMandatoryResponse changePermission(ChangePermissionForFileRoleRequest request) {
        return null;
    }

    @Override
    public FileContentResponse read(FileContentActionRequest request) {
        return null;
    }

    @Override
    public FileContentResponse write(FileContentActionRequest request) {
        return null;
    }

    @Override
    public FileContentResponse execute(FileContentActionRequest request) {
        return null;
    }
}
