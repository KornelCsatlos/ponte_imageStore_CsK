package hu.ponte.hr.exception;

import lombok.Getter;

@Getter
public class VerificationFailedException extends RuntimeException{
    private final String message;

    public VerificationFailedException(String message){
        this.message = message;
    }
}
