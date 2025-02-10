package com.sac_lishchuk.shared.exception;

import com.sac_lishchuk.config.exception.SacBaseException;

import java.lang.constant.ConstantDesc;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UnknownFileException extends SacBaseException {

    public UnknownFileException(String fileName) {
        super(
                "файл %s не був знайдений".formatted(fileName),
                "файл %s не був знайдений, будь-ласка введіть його коректну назву".formatted(fileName),
                "FILE_ISSUE",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
