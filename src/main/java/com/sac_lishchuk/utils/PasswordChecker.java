package com.sac_lishchuk.utils;

import com.sac_lishchuk.config.BusinessOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordChecker {

    private final BusinessOptions businessOptions;

    public boolean isValidPasswordComplexity(String password, Boolean isComplex) {
        var length = password.length();
        var config = businessOptions.getPasswordConfig();
        return !isComplex || length >= config.getMinComplexLength();
    }
}
