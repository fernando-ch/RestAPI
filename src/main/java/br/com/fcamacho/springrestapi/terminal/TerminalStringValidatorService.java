package br.com.fcamacho.springrestapi.terminal;

import br.com.fcamacho.springrestapi.genericValidation.ValidationErrorDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TerminalStringValidatorService {

    private static final int NUM_FIELDS = 10;

    ValidationErrorDTO performValidations(String terminalString) {
        String[] properties = StringUtils.delimitedListToStringArray(terminalString, ";");
        ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();

        if (properties.length < NUM_FIELDS) {
            validationErrorDTO.addError("Not all comma separated values are present, should be " + NUM_FIELDS);
            return validationErrorDTO;
        }

        if (properties.length > NUM_FIELDS) {
            validationErrorDTO.addError("Too many comma separated values are present, should be " + NUM_FIELDS);
            return validationErrorDTO;
        }

        String logic = properties[0];
        String serial = properties[1];
        String model = properties[2];
        String sam = properties[3];
        String plat = properties[5];
        String version = properties[6];
        String mxr = properties[7];
        String mxf = properties[8];

        if (StringUtils.isEmpty(logic)) {
            validationErrorDTO.addFieldError("logic", "This property is required");
        }
        else if (isNotAnInteger(logic)) {
            validationErrorDTO.addFieldError("logic", "Should be an integer value");
        }

        if (StringUtils.isEmpty(serial)) {
            validationErrorDTO.addFieldError("serial", "This property is required");
        }

        if (StringUtils.isEmpty(model)) {
            validationErrorDTO.addFieldError("model", "This property is required");
        }

        if (!StringUtils.isEmpty(sam) && (isNotAnInteger(sam) || Integer.parseInt(sam) < 0)) {
            validationErrorDTO.addFieldError("sam", "Should be an integer greater than or equal zero");
        }

        if (!StringUtils.isEmpty(plat) && isNotAnInteger(plat)) {
            validationErrorDTO.addFieldError("plat", "Should be an integer value");
        }

        if (StringUtils.isEmpty(version)) {
            validationErrorDTO.addFieldError("version", "This property is required");
        }

        if (!StringUtils.isEmpty(mxr) && isNotAnInteger(mxr)) {
            validationErrorDTO.addFieldError("mxr", "Should be an integer value");
        }

        if (!StringUtils.isEmpty(mxf) && isNotAnInteger(mxf)) {
            validationErrorDTO.addFieldError("mxf", "Should be an integer value");
        }

        return validationErrorDTO;
    }

    private boolean isNotAnInteger(String s) {
        try {
            Integer.parseInt(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
