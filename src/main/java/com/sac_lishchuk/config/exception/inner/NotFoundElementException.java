package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NotFoundElementException extends SacBaseException {
    public NotFoundElementException(Class<?> aClass, Object userId) {
        super(
                "%s з %s не знайдений в бд".formatted(aClass.getSimpleName(), userId),
                "введіть параметрм для пошуку існуючого запису в бд",
                "NOT_FOUND_USER",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
