package ua.foxminded.chyzhov.carrestservice.util.exceptions;

import lombok.Getter;

@Getter
public class RecordAlreadyExists extends RuntimeException {

    private final String entityName;
    private final String fieldName;
    private final String fieldValue;


    public RecordAlreadyExists(String entityName, String fieldName, String fieldValue) {

        super(String.format("%s with %s: [%s] already exists", entityName, fieldName, fieldValue));
        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
