package com.Jedi.OnePlacementServer.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResourceNotFoundException extends RuntimeException{
    String resName;
    String fieldName;
    long fieldVal;

    public ResourceNotFoundException(String resName, String fieldName, long fieldVal) {
        super(String.format("%s not found with %s : %s", resName, fieldName, fieldVal));
        this.resName = resName;
        this.fieldName = fieldName;
        this.fieldVal = fieldVal;
    }
}
