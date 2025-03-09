package com.sac_lishchuk.service;

import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.response.FileContentResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface FileServiceI {
    FileContentResponse read(FileContentActionRequest request);
    ResponseEntity<InputStreamResource> readImage(FileContentActionRequest request);
    FileContentResponse write(FileContentActionRequest request);
    FileContentResponse execute(FileContentActionRequest request);
}
