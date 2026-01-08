package com.fuzis.util;

import com.fuzis.exception.ValidationException;
import com.fuzis.exception.YamlSyntaxException;
import com.fuzis.transferdata.ErrorDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

@Slf4j
@Provider
public class ErrorHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof ConstraintViolationException exp) {
            ErrorDTO error = new ErrorDTO(
                    false,
                    "ConstraintViolation",
                    exp.getMessage()
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        } else if (exception instanceof ValidationException exp) {
            ErrorDTO error = new ErrorDTO(
                    false,
                    "ValidationError",
                    exp.getMessage()
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        } else if (exception instanceof YamlSyntaxException exp) {
            ErrorDTO error = new ErrorDTO(
                    false,
                    "YamlSyntaxError",
                    exp.getMessage()
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        } else {
            ErrorDTO error = new ErrorDTO(
                    false,
                    "UnknownException: " + exception.getClass().getSimpleName(),
                    exception.getMessage()
            );
            log.error("Unhandled exception", exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }
}