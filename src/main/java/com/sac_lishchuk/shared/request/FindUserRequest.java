package com.sac_lishchuk.shared.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
}
