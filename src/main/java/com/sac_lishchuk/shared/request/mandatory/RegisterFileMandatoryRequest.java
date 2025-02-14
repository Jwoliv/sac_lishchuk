package com.sac_lishchuk.shared.request.mandatory;

import com.sac_lishchuk.enums.MandatoryLevel;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
import com.sac_lishchuk.shared.request.UserConfig;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFileMandatoryRequest {
    @NotNull
    private String fileName;
    @Builder.Default
    private MandatoryLevel mandatoryLevel = MandatoryLevel.PUBLIC;
    @NotNull
    private UserConfig adminConfig;
}
