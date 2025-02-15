package com.sac_lishchuk.controller;

import com.sac_lishchuk.service.FileRuleServiceI;
import com.sac_lishchuk.shared.request.role.ChangePermissionForFileRoleRequest;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.role.RegisterFileRoleRequest;
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
@RequestMapping("/api/rule/files")
public class FileRuleController {
    private final FileRuleServiceI fileRuleService;


    @PostMapping("/register")
    public FileRoleRegisterResponse registerFile(@RequestBody @Valid RegisterFileRoleRequest request) {
        return fileRuleService.register(request);
    }

    @PostMapping("/read")
    public FileContentResponse readFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileRuleService.read(request);
    }

    @PostMapping("/write")
    public FileContentResponse writeFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileRuleService.write(request);
    }

    @PostMapping("/execute")
    public FileContentResponse executeFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileRuleService.execute(request);
    }

    @PostMapping("/change-permission")
    public ChangePermissionRoleResponse changePermission(@RequestBody @Valid ChangePermissionForFileRoleRequest request) {
        return fileRuleService.changePermission(request);
    }
}
