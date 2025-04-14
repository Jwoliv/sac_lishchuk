package com.sac_lishchuk.config.exception.inner;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class CanNotCreateAdminException extends SacBaseException {
    public CanNotCreateAdminException() {
        super(
                "вже створено два адміна із правами доступу",
                "вже створено два адміна із правами доступу",
                "INVALID_PASSWORD",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
