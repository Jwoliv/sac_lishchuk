package com.sac_lishchuk.shared.request;

import com.sac_lishchuk.enums.Role;
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
    private Boolean isComplexPassword;
    private AdminConfig adminConfig;
    private Role role;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminConfig {
        private String email;
        private String password;
    }
}
