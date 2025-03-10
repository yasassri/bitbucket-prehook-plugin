package org.ycr.bitbucket.exceptions;

public class TagValidationException extends RuntimeException {
    public TagValidationException(String message) {
        super(message);
    }

    public TagValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
