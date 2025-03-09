package com.sac_lishchuk.controller;

import com.sac_lishchuk.model.discretionary.DiscretionaryMatrix;
import com.sac_lishchuk.service.FileDiscretionaryServiceI;
import com.sac_lishchuk.shared.request.ChangePermissionDiscretionaryRequest;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.request.RegisterFileDiscretionaryRequest;
import com.sac_lishchuk.shared.request.mandatory.ChangePermissionForFileMandatoryRequest;
import com.sac_lishchuk.shared.response.FileContentResponse;
import com.sac_lishchuk.shared.response.mandatory.ChangePermissionMandatoryResponse;
import com.sac_lishchuk.shared.response.mandatory.FileMandatoryRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discretionary/files")
public class FileDiscretionaryController {
    private final FileDiscretionaryServiceI fileDiscretionaryService;


    @PostMapping("/register")
    public DiscretionaryMatrix registerFile(@RequestBody @Valid RegisterFileDiscretionaryRequest request) {
        return fileDiscretionaryService.register(request);
    }

    @PostMapping("/read")
    public FileContentResponse readFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileDiscretionaryService.read(request);
    }

    @PostMapping("/image/read")
    public ResponseEntity<InputStreamResource> readImage(@RequestBody FileContentActionRequest request) {
        return fileDiscretionaryService.readImage(request);
    }


    @PostMapping("/write")
    public FileContentResponse writeFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileDiscretionaryService.write(request);
    }

    @PostMapping("/execute")
    public FileContentResponse executeFile(@RequestBody @Valid FileContentActionRequest request) {
        return fileDiscretionaryService.execute(request);
    }

    @PostMapping("/change-permission")
    public DiscretionaryMatrix changePermission(@RequestBody @Valid ChangePermissionDiscretionaryRequest request) {
        return fileDiscretionaryService.changePermission(request);
    }
}
