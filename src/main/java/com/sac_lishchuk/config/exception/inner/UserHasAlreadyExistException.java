package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UserHasAlreadyExistException extends SacBaseException {
    public UserHasAlreadyExistException(String email) {
        super(
                "користувач %s вже існує".formatted(email),
                "введіть інший email для створення користувача, цей вже використовується",
                "TAKEN_EMAIL",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
