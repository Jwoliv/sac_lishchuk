package com.sac_lishchuk.controller.advice;

import com.sac_lishchuk.config.exception.inner.InvalidPasswordException;
import com.sac_lishchuk.config.exception.inner.NotFoundElementException;
import com.sac_lishchuk.config.exception.inner.UserHasAlreadyExistException;
import com.sac_lishchuk.mapper.ExceptionMapper;
import com.sac_lishchuk.shared.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {
    private final ExceptionMapper exceptionHandler;

    @ExceptionHandler(InvalidPasswordException.class)
    public ExceptionResponse invalidPassword(InvalidPasswordException ex) {
        return exceptionHandler.mapToExceptionResponse(ex);
    }

    @ExceptionHandler(NotFoundElementException.class)
    public ExceptionResponse notFoundElement(NotFoundElementException ex) {
        return exceptionHandler.mapToExceptionResponse(ex);
    }

    @ExceptionHandler(UserHasAlreadyExistException.class)
    public ExceptionResponse userHasAlreadyExist(UserHasAlreadyExistException ex) {
        return exceptionHandler.mapToExceptionResponse(ex);
    }
}
