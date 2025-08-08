package dev.steelbookdb.steelbookapi.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String field, String value) {
        super("Field '" + field + "' with value '" + value + "' already exists.");
    }
}
