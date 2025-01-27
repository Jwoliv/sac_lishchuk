package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class InvalidPasswordException extends SacBaseException {
    public InvalidPasswordException(Long userId, String password) {
        super(
                getMsgTxt(userId, password),
                "password isn't valid, please repeat attempt",
                "INVALID_PASSWORD",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }

    private static String getMsgTxt(Long userId, String password) {
        return Objects.nonNull(userId)
                ? "password for user with %d isn't valid due to complex reason password [%s]".formatted(userId, password)
                : "new user entered password isn't valid due to complex reason password [%s]".formatted(password);
    }
}
