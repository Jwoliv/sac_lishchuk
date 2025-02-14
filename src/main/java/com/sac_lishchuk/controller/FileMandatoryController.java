package com.sac_lishchuk.controller;

import com.sac_lishchuk.service.FileMandatoryServiceI;
import com.sac_lishchuk.shared.request.mandatory.ChangePermissionForFileMandatoryRequest;
import com.sac_lishchuk.shared.request.mandatory.RegisterFileMandatoryRequest;
import com.sac_lishchuk.shared.request.role.ChangePermissionForFileRoleRequest;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.response.mandatory.ChangePermissionMandatoryResponse;
import com.sac_lishchuk.shared.response.mandatory.FileMandatoryRegisterResponse;
import com.sac_lishchuk.shared.response.role.ChangePermissionRoleResponse;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.role.FileRoleRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mandatory/files")
public class FileMandatoryController {
    private final FileMandatoryServiceI fileMandatoryService;


    @PostMapping("/register")
    public FileMandatoryRegisterResponse registerFile(@RequestBody @Valid RegisterFileMandatoryRequest request) {
        return fileMandatoryService.register(request);
    }

    @PostMapping("/read")
    public FileContentResponse readFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileMandatoryService.read(request);
    }

    @PostMapping("/write")
    public FileContentResponse writeFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileMandatoryService.write(request);
    }

    @PostMapping("/execute")
    public FileContentResponse executeFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileMandatoryService.execute(request);
    }

    @PostMapping("/change-permission")
    public ChangePermissionMandatoryResponse changePermission(@RequestBody @Valid ChangePermissionForFileMandatoryRequest request) {
        return fileMandatoryService.changePermission(request);
    }
}
