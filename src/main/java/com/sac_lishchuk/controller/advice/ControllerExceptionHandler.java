package com.sac_lishchuk.controller.advice;

import com.sac_lishchuk.config.exception.SacBaseException;
import com.sac_lishchuk.mapper.ExceptionMapper;
import com.sac_lishchuk.shared.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {
    private final ExceptionMapper exceptionHandler;

    @ExceptionHandler(SacBaseException.class)
    public ExceptionResponse handleSacException(SacBaseException ex) {
        return exceptionHandler.mapToExceptionResponse(ex);
    }

}
