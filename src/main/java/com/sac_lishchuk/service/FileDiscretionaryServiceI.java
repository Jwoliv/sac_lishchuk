package com.sac_lishchuk.service;

import com.sac_lishchuk.model.discretionary.DiscretionaryMatrix;
import com.sac_lishchuk.shared.request.ChangePermissionDiscretionaryRequest;
import com.sac_lishchuk.shared.request.RegisterFileDiscretionaryRequest;
import com.sac_lishchuk.shared.response.mandatory.ChangePermissionMandatoryResponse;

public interface FileDiscretionaryServiceI extends FileServiceI {
    DiscretionaryMatrix register(RegisterFileDiscretionaryRequest request);
    DiscretionaryMatrix changePermission(ChangePermissionDiscretionaryRequest request);
}
