package hu.ponte.hr.exception;

import lombok.Getter;

@Getter
public class UnableToCreateSignedHashException extends RuntimeException{
    private final String message;

    public UnableToCreateSignedHashException(String message){
        this.message = message;
    }
}
