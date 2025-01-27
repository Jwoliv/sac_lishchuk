package com.sac_lishchuk.shared.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessChangedPasswordResponse {
    private Long userId;
    private LocalDateTime changedAt;
    private String oldPassword;
    private String newPassword;
}
