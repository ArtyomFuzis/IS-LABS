package com.fuzis.exception;

public class YamlSyntaxException extends RuntimeException {
    public YamlSyntaxException(String message) {
        super(message);
    }

    public YamlSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }
}