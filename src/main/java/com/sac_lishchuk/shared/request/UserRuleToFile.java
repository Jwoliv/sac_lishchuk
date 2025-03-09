package com.sac_lishchuk.shared.request;

import com.sac_lishchuk.enums.Rule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRuleToFile {
    private String email;
    private List<Rule> rules;
}