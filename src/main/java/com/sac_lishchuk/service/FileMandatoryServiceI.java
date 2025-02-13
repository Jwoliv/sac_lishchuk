package com.sac_lishchuk.service;

import com.sac_lishchuk.shared.request.mandatory.RegisterFileMandatoryRequest;
import com.sac_lishchuk.shared.request.role.ChangePermissionForFileRoleRequest;
import com.sac_lishchuk.shared.response.mandatory.ChangePermissionMandatoryResponse;
import com.sac_lishchuk.shared.response.mandatory.FileMandatoryRegisterResponse;

public interface FileMandatoryServiceI extends FileServiceI {
    FileMandatoryRegisterResponse register(RegisterFileMandatoryRequest request);
    ChangePermissionMandatoryResponse changePermission(ChangePermissionForFileRoleRequest request);
}
