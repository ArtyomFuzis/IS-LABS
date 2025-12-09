package com.fuzis.controller;

import com.fuzis.service.YamlImportService;
import com.fuzis.exception.ValidationException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Path("/operations/bulk")
public class BulkOperationsController {

    @Inject
    private YamlImportService yamlImportService;

    @POST
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response bulkImportYaml(String yamlContent) {
        try {
            if (yamlContent == null || yamlContent.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createResponse("error", "YAML content is empty"))
                        .build();
            }

            ByteArrayInputStream inputStream = new ByteArrayInputStream(yamlContent.getBytes());
            yamlImportService.importYaml(inputStream);

            return Response.ok(createResponse("success", "YAML successfully imported in transaction"))
                    .build();

        } catch (ValidationException e) {
            // Обработка кастомных ошибок валидации
            List<String> errorMessages = e.getErrorMessages();
            String errorMessage = "Validation errors: " + String.join("; ", errorMessages);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createResponse("error", errorMessage))
                    .build();

        } catch (jakarta.validation.ConstraintViolationException e) {
            // Ошибки валидации (constraint violations)
            StringBuilder violations = new StringBuilder();
            e.getConstraintViolations().forEach(v ->
                    violations.append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("; "));

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createResponse("error", "Validation errors: " + violations))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createResponse("error", "Validation error: " + e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createResponse("error", "Failed to import YAML: " +
                            getRootCauseMessage(e)))
                    .build();
        }
    }

    private String createResponse(String status, String message) {
        return String.format("{\"status\":\"%s\",\"message\":\"%s\"}",
                status, escapeJson(message));
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String getRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}