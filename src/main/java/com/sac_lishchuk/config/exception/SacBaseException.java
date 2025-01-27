package com.sac_lishchuk.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SacBaseException extends RuntimeException {
    private final String msgText;
    private final String clientText;
    private final String code;
    private final LocalDateTime occurredAt;
}
