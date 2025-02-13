package com.sac_lishchuk.shared.response.role;

import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRoleRegisterResponse {
    private String fileName;
    private Map<Role, List<Rule>> permissions;
    private LocalDateTime occurAt;
}
