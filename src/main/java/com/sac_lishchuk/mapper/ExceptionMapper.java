package com.sac_lishchuk.mapper;

import com.sac_lishchuk.config.exception.SacBaseException;
import com.sac_lishchuk.shared.exception.ExceptionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {
    ExceptionResponse mapToExceptionResponse(SacBaseException ex);
}
