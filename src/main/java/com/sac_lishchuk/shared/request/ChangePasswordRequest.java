package com.sac_lishchuk.shared.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    private String email;
    private String password;
    private Boolean isComplexPassword;
    private Long daysToExpire;
}
