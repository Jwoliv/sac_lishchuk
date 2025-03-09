package com.sac_lishchuk.shared.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePermissionDiscretionaryRequest {
    @NotNull
    private String fileName;
    @NotNull
    private UserConfig adminConfig;
    private UserRuleToFile userRuleToFile;
    private LocalDateTime sinceAccess;
    private LocalDateTime toAccess;
    @NotNull
    private ChangePermissionAction action;

    public enum ChangePermissionAction {
        ADD, REMOVE
    }
}
