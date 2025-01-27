package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UserHasAlreadyExistException extends SacBaseException {
    public UserHasAlreadyExistException(String email) {
        super(
                "use with this %s has already exists".formatted(email),
                "please use another email to create user, this one has already taken",
                "TAKEN_EMAIL",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
