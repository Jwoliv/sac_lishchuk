package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NotFoundElementException extends SacBaseException {
    public NotFoundElementException(Class<?> aClass, Object userId) {
        super(
                "can't find %s with %s in database".formatted(aClass.getSimpleName(), userId),
                "please enter the id of existence user in the system",
                "NOT_FOUND_USER",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
