package com.sac_lishchuk.shared.request;

import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
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
public class RegisterFileRequest {
    @NotNull
    private String fileName;
    @Size(min = 1, max = 100)
    private Map<Role, List<Rule>> permissions;
    @NotNull
    private UserConfig adminConfig;
}
