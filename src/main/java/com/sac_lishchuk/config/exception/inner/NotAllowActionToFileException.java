package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;
import com.sac_lishchuk.enums.Rule;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NotAllowActionToFileException extends SacBaseException {
    public NotAllowActionToFileException(String username, Rule fileAction, String fileName) {
        super(
                "користувач %s намагався отримати %s доступ по %s файлу".formatted(username, fileAction, fileName),
                "ви не маєте доступу до даного файлу, повідомте адміністратора",
                "SECURITY_ISSUE",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    public NotAllowActionToFileException(String username, String fileAction, String fileName) {
        super(
                "користувач %s намагався отримати %s доступ по %s файлу".formatted(username, fileAction, fileName),
                "ви не маєте доступу до даного файлу, повідомте адміністратора",
                "SECURITY_ISSUE",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
