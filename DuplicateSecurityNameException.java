package org.investment;

public class DuplicateSecurityNameException extends RuntimeException {
    public DuplicateSecurityNameException(String message) {
        super(message);
    }
}
