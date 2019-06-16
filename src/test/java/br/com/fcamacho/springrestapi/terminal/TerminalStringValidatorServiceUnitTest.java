package br.com.fcamacho.springrestapi.terminal;

import br.com.fcamacho.springrestapi.genericValidation.ValidationErrorDTO;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TerminalStringValidatorServiceUnitTest {

    private static final int NUM_FIELDS = 10;

    private TerminalStringValidatorService terminalStringValidatorService = new TerminalStringValidatorService();

    @Test
    public void shouldAddErrorIfNotAllCommasArePresent() {
        String terminalString = "44332211;123;PWWIN;0;";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertTrue(validationErrorDTO.hasErrors());assertThat(validationErrorDTO.getErrors(), hasSize(1));
        assertThat(validationErrorDTO.getErrors().get(0), is("Not all comma separated values are present, should be " + NUM_FIELDS));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(0));
    }

    @Test
    public void shouldAddErrorIfMoreFieldsThanExpectedArePresent() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;0;16777216;PWWIN;";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);
        
        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(1));
        assertThat(validationErrorDTO.getErrors().get(0), is("Too many comma separated values are present, should be " + NUM_FIELDS));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(0));
    }

    @Test
    public void shouldAddErrorIfLogicIsNotAnInteger() {
        String terminalString = "4a4332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);
        
        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("logic"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("Should be an integer value"));
    }

    @Test
    public void shouldAddErrorIfLogicIsNotPresent() {
        String terminalString = ";123;PWWIN;0;F04A2E4088B;4;8.00b3;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);
        
        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("logic"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("This property is required"));
    }

    @Test
    public void shouldAddErrorIfSerialIsNotPresent() {
        String terminalString = "44332211;;PWWIN;0;F04A2E4088B;4;8.00b3;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);
        
        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("serial"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("This property is required"));
    }

    @Test
    public void shouldAddErrorIfModelIsNotPresent() {
        String terminalString = "44332211;123;;0;F04A2E4088B;4;8.00b3;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);
        
        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("model"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("This property is required"));
    }

    @Test
    public void shouldAddErrorIfSamIsNotAnInteger() {
        String terminalString = "44332211;123;PWWIN;a0;F04A2E4088B;4;8.00b3;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);
        
        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("sam"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("Should be an integer greater than or equal zero"));
    }

    @Test
    public void shouldAddErrorIfSamIsLessThanZero() {
        String terminalString = "44332211;123;PWWIN;-2;F04A2E4088B;4;8.00b3;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);
        
        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("sam"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("Should be an integer greater than or equal zero"));
    }

    @Test
    public void shouldAddErrorIfPlatIsNotAnInteger() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;b4;8.00b3;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);
        
        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("plat"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("Should be an integer value"));
    }

    @Test
    public void shouldAddErrorIfMxrIsNotAnInteger() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;0c;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("mxr"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("Should be an integer value"));
    }

    @Test
    public void shouldAddErrorIfMxfIsNotAnInteger() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;0;167d77216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("mxf"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("Should be an integer value"));
    }

    @Test
    public void shouldAddErrorIfVersionIsNotPresent() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("version"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("This property is required"));
    }

    @Test
    public void shouldAddMoreThenOnErrorIfMoreThanOneFieldIsInvalid() {
        String terminalString = ";123;PWWIN;0;F04A2E4088B;4;;0;167d77216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(3));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("logic"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("This property is required"));
        assertThat(validationErrorDTO.getFieldErrors().get(1).getField(), is("version"));
        assertThat(validationErrorDTO.getFieldErrors().get(1).getMessage(), is("This property is required"));
        assertThat(validationErrorDTO.getFieldErrors().get(2).getField(), is("mxf"));
        assertThat(validationErrorDTO.getFieldErrors().get(2).getMessage(), is("Should be an integer value"));
    }

    @Test
    public void shouldIgnoreFieldErrorsIfTotalFieldsIsLessThanTheSpected() {
        String terminalString = ";123;PWWIN;0;F04A2E4088B;4;8.00b3;0;167d77216";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(1));
        assertThat(validationErrorDTO.getErrors().get(0), is("Not all comma separated values are present, should be " + NUM_FIELDS));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(0));
    }

    @Test
    public void shouldIgnoreFieldErrorsIfTotalFieldsIsGreaterThanTheSpected() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;0;167d77216;PWWIN;";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(1));
        assertThat(validationErrorDTO.getErrors().get(0), is("Too many comma separated values are present, should be " + NUM_FIELDS));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(0));
    }

    @Test
    public void shouldNotAddErrorIfAllFieldsAreValid() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;0;16777216;PWWIN";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertFalse(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(0));
    }

    @Test
    public void shouldNotAddErrorIfOnlyOptionalFieldsAreMissing() {
        String terminalString = "44332211;123;PWWIN;;;;8.00b3;;;";
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        assertFalse(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(0));
    }
}
