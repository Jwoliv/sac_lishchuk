package com.sac_lishchuk.shared.request;

import com.sac_lishchuk.enums.MandatoryLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMandatoryPermissionRequest {
    @NotNull
    private String email;
    @NotNull
    private MandatoryLevel mandatoryLevel;
    @Valid
    private UserConfig adminConfig;
}
