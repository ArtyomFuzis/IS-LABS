package com.fuzis.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<ValidationError> validationErrors;

    public ValidationException(String message) {
        super(message);
        this.validationErrors = new ArrayList<>();
        this.validationErrors.add(new ValidationError("VALIDATION_ERROR", message));
    }

    public ValidationException(String errorCode, String message) {
        super(message);
        this.validationErrors = new ArrayList<>();
        this.validationErrors.add(new ValidationError(errorCode, message));
    }

    public ValidationException(List<ValidationError> validationErrors) {
        super("Validation failed with " + validationErrors.size() + " error(s)");
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.validationErrors = new ArrayList<>();
        this.validationErrors.add(new ValidationError("VALIDATION_ERROR", message));
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public List<String> getErrorMessages() {
        List<String> messages = new ArrayList<>();
        for (ValidationError error : validationErrors) {
            messages.add(error.getMessage());
        }
        return messages;
    }

    @Override
    public String getMessage() {
        if (validationErrors.size() == 1) {
            return validationErrors.get(0).getMessage();
        } else {
            StringBuilder sb = new StringBuilder("Validation failed with ");
            sb.append(validationErrors.size()).append(" error(s): ");
            for (int i = 0; i < validationErrors.size(); i++) {
                if (i > 0) sb.append("; ");
                sb.append(validationErrors.get(i).getMessage());
            }
            return sb.toString();
        }
    }

    public static class ValidationError {
        private final String errorCode;
        private final String message;

        public ValidationError(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }
    }
}