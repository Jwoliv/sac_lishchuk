package com.sac_lishchuk.shared.request;

import com.sac_lishchuk.enums.Rule;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterFileDiscretionaryRequest {
    @NotNull
    private String fileName;
    @NotNull
    private UserConfig adminConfig;
    private UserRuleToFile userRuleToFile;
}
