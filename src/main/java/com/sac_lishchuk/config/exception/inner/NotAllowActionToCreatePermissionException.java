package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NotAllowActionToCreatePermissionException extends SacBaseException {
    public NotAllowActionToCreatePermissionException(String fileName)
    {
        super(
                "спроба реєстрація файла %s не дозволеним користувачем".formatted(fileName),
                "введіть конфіг адміністратора або модератора",
                "SECURITY_ISSUE",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
