package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NotAllowActionToCreateUserException extends SacBaseException {
    public NotAllowActionToCreateUserException() {
        super(
                "спроба створення користувача не з ролю User користувачем із не уповноваженою ролю",
                "введіть конфіг адміністратора або ж створюйте користувача з ролю User",
                "SECURITY_ISSUE",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
