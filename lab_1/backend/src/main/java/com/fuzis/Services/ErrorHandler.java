package com.fuzis.Services;

import com.fuzis.Data.ErrorDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;

@Slf4j
@Provider
public class ErrorHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        log.error("ConstrainViolation: ", exception);

        ErrorDTO error = new ErrorDTO(
                false,
                "ConstrainViolation"
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}
