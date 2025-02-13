package com.sac_lishchuk.service;

import com.sac_lishchuk.shared.request.role.ChangePermissionForFileRoleRequest;
import com.sac_lishchuk.shared.request.role.RegisterFileRoleRequest;
import com.sac_lishchuk.shared.response.role.ChangePermissionRoleResponse;
import com.sac_lishchuk.shared.response.role.FileRoleRegisterResponse;

public interface FileRuleServiceI extends FileServiceI {
    FileRoleRegisterResponse register(RegisterFileRoleRequest request);
    ChangePermissionRoleResponse changePermission(ChangePermissionForFileRoleRequest request);
}
