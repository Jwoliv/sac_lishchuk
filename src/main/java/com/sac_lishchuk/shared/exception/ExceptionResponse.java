package com.sac_lishchuk.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private final String msgText;
    private final String clientText;
    private final String code;
    private final LocalDateTime occurredAt;
}
