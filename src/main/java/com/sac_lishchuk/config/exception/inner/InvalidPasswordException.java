package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class InvalidPasswordException extends SacBaseException {
    public InvalidPasswordException(Long userId, String password) {
        super(
                getMsgTxt(userId, password),
                "пароль не є валідним повторітю спробу",
                "INVALID_PASSWORD",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }

    private static String getMsgTxt(Long userId, String password) {
        return Objects.nonNull(userId)
                ? "користувач %d ввів не валідний пароль [%s]".formatted(userId, password)
                : "новий користувач ввів не валідний пароль [%s]".formatted(password);
    }
}
