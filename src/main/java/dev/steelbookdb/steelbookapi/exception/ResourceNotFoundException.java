package dev.steelbookdb.steelbookapi.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entity, Long id) {
        super(String.format("%s not found with ID: %d", capitalizeFirstLetter(entity), id));
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
