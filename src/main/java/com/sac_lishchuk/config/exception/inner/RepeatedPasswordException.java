package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class RepeatedPasswordException extends SacBaseException {
    public RepeatedPasswordException() {
        super(
                "наданий пароль був нещодавно використаний для даного користувача, будь-ласка змініть пароль на той який не використовувався в системі отсаннім часом",
                "наданий пароль був нещодавно використаний для даного користувача, будь-ласка змініть пароль на той який не використовувався в системі отсаннім часом",
                "INVALID_PASSWORD",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
