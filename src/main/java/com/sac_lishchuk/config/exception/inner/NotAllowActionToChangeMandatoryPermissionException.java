package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NotAllowActionToChangeMandatoryPermissionException extends SacBaseException {
    public NotAllowActionToChangeMandatoryPermissionException(String fileName)
    {
        super(
                "спроба зміни мандатного рівня не дозволена для користувача %s".formatted(fileName),
                "введіть конфіг адміністратора",
                "SECURITY_ISSUE",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
