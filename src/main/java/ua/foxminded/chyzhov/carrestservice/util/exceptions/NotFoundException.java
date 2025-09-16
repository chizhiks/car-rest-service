package ua.foxminded.chyzhov.carrestservice.util.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final String entityName;
    private final String identifier;

    public NotFoundException(String entityName, String identifier) {

        super(String.format("%s with identifier [%s] not found", entityName, identifier));
        this.entityName = entityName;
        this.identifier = identifier;
    }

    public NotFoundException(String entityName, Integer identifier) {

        super(String.format("%s with identifier [%s] not found", entityName, identifier));
        this.entityName = entityName;
        this.identifier = String.valueOf(identifier);
    }
}
