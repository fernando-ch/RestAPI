package br.com.fcamacho.springrestapi.genericValidation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO {

    private final List<String> errors = new ArrayList<>();
    private final List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }

    public void addFieldError(String field, String message) {
        FieldErrorDTO error = new FieldErrorDTO(field, message);
        fieldErrors.add(error);
    }

    public void addError(String message) {
        errors.add(message);
    }

    public boolean hasErrors() {
        return !errors.isEmpty() || !fieldErrors.isEmpty();
    }
}
