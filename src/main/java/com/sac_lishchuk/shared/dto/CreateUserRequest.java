package com.sac_lishchuk.shared.dto;

import com.sac_lishchuk.enums.MandatoryLevel;
import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.shared.request.UserConfig;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String middleName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @Builder.Default
    private Boolean isComplexPassword = false;
    private UserConfig adminConfig;
    @Builder.Default
    private Role role = Role.USER;
    private MandatoryLevel mandatoryLevel;
}
