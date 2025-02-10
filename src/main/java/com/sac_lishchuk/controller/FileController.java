package com.sac_lishchuk.controller;

import com.sac_lishchuk.service.FileService;
import com.sac_lishchuk.shared.request.ChangePermissionRequest;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.RegisterFileRequest;
import com.sac_lishchuk.shared.response.ChangePermissionResponse;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.FileRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;


    @PostMapping("/register")
    public FileRegisterResponse registerFile(@RequestBody @Valid RegisterFileRequest request) {
        return fileService.register(request);
    }

    @PostMapping("/read")
    public FileContentResponse readFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileService.read(request);
    }

    @PostMapping("/write")
    public FileContentResponse writeFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileService.write(request);
    }

    @PostMapping("/execute")
    public FileContentResponse executeFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileService.execute(request);
    }

    @PostMapping("/change-permission")
    public ChangePermissionResponse changePermission(@RequestBody @Valid ChangePermissionRequest request) {
        return fileService.changePermission(request);
    }
}
