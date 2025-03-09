package com.sac_lishchuk.controller;

import com.sac_lishchuk.service.FileRuleServiceI;
import com.sac_lishchuk.shared.request.role.ChangePermissionForFileRoleRequest;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.role.RegisterFileRoleRequest;
import com.sac_lishchuk.shared.response.role.ChangePermissionRoleResponse;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.role.FileRoleRegisterResponse;
import com.sac_lishchuk.utils.FileActionExecutor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

import static com.sac_lishchuk.utils.FileActionExecutor.getMediaTypeForFileExtension;

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

    @PostMapping("/image/read")
    public ResponseEntity<InputStreamResource> readImage(@RequestBody FileContentActionRequest request) {
        return fileRuleService.readImage(request);
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
