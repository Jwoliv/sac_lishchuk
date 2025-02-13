package com.sac_lishchuk.service;

import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.response.FileContentResponse;

public interface FileServiceI {
    FileContentResponse read(FileContentActionRequest request);
    FileContentResponse write(FileContentActionRequest request);
    FileContentResponse execute(FileContentActionRequest request);
}
