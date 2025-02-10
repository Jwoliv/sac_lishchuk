package com.sac_lishchuk.service;

import com.sac_lishchuk.shared.request.ChangePermissionRequest;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.RegisterFileRequest;
import com.sac_lishchuk.shared.response.ChangePermissionResponse;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.FileRegisterResponse;

public interface FileService {
    FileRegisterResponse register(RegisterFileRequest request);
    FileContentResponse read(FileContentActionRequest request);
    FileContentResponse write(FileContentActionRequest request);
    FileContentResponse execute(FileContentActionRequest request);
    ChangePermissionResponse changePermission(ChangePermissionRequest request);
}
