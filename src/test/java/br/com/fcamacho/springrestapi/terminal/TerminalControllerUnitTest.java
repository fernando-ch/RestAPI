package br.com.fcamacho.springrestapi.terminal;

import br.com.fcamacho.springrestapi.genericValidation.ValidationErrorDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TerminalController.class)
public class TerminalControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerminalStringValidatorService terminalStringValidatorServiceMock;

    private static final String BASE_URL = "/v1/terminal";

    @Test
    public void shouldRejectPostIfContentTypeIsNotTextHml() throws Exception {
        this.mockMvc.perform(post(BASE_URL).content("")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnsupportedMediaType());

        this.mockMvc.perform(post(BASE_URL).content("")
                .contentType(MediaType.TEXT_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());

        this.mockMvc.perform(post(BASE_URL).content("")
                .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isUnsupportedMediaType());

        Mockito.verifyZeroInteractions(terminalStringValidatorServiceMock);
    }

    @Test
    public void shouldReturnBadRequestIfBodyDidntPassValidation() throws Exception {
        String body = "123123123";

        ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();
        validationErrorDTO.addError("global message");
        validationErrorDTO.addFieldError("fieldName1", "error message 1");
        validationErrorDTO.addFieldError("fieldName2", "error message 2");

        when(terminalStringValidatorServiceMock.performValidations(body)).thenReturn(validationErrorDTO);

        MockHttpServletRequestBuilder builder = post(BASE_URL).contentType(MediaType.TEXT_HTML_VALUE).content(body);
        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("global message")))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[0].field", is("fieldName1")))
                .andExpect(jsonPath("$.fieldErrors[0].message", is("error message 1")))
                .andExpect(jsonPath("$.fieldErrors[1].field", is("fieldName2")))
                .andExpect(jsonPath("$.fieldErrors[1].message", is("error message 2")));

        Mockito.verify(terminalStringValidatorServiceMock, only()).performValidations(body);
    }
}
