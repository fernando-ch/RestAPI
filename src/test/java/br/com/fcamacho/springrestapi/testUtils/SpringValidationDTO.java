package br.com.fcamacho.springrestapi.testUtils;

import java.util.List;

public class SpringValidationDTO {

    private List<SpringValidationErrorDTO> errors;

    public List<SpringValidationErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<SpringValidationErrorDTO> errors) {
        this.errors = errors;
    }
}
