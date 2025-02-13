package com.sac_lishchuk.shared.request.mandatory;

import com.sac_lishchuk.enums.MandatoryLevel;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
import com.sac_lishchuk.shared.request.UserConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePermissionForFileMandatoryRequest {
    @NotNull
    private String fileName;
    @Size(min = 1, max = 100)
    private MandatoryLevel mandatoryLevel;
    @NotNull
    private ChangePermissionAction action;
    @Valid
    private UserConfig userConfig;

    public enum ChangePermissionAction {
        ADD, REMOVE
    }
}
