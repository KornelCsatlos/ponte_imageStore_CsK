package hu.ponte.hr.exception;

import lombok.Getter;

@Getter
public class UnableToLoadKeyException extends RuntimeException {

    private final String message;

    public UnableToLoadKeyException(Exception e) {
        message = e.getMessage();
    }
}
