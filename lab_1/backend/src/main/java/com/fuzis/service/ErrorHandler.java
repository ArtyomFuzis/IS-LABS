package com.fuzis.service;

import com.fuzis.transferdata.ErrorDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

@Slf4j
@Provider
public class ErrorHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        log.error("ConstrainViolation: ", exception);

        ErrorDTO error = new ErrorDTO(
                false,
                "ConstrainViolation",
                exception.getErrorMessage()
        );
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}
