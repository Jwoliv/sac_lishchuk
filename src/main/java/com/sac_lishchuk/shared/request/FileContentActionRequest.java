package com.sac_lishchuk.shared.request;

import com.sac_lishchuk.enums.Rule;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class FileContentActionRequest {
    @NotNull
    private String fileName;
    @Valid
    private UserConfig userConfig;
    private String ipAddress;
    private String newContent;
    private String targetContent;
    private FileAction action;

    public enum FileAction {
        APPEND, REMOVE, UPDATE, OVERWRITE
    }

}
