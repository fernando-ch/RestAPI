package br.com.fcamacho.springrestapi.terminal;

import br.com.fcamacho.springrestapi.genericValidation.ValidationErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TerminalController.class)
public class TerminalControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerminalStringValidatorService terminalStringValidatorServiceMock;

    @MockBean
    private TerminalService terminalServiceMock;

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

        Mockito.verifyZeroInteractions(terminalStringValidatorServiceMock, terminalServiceMock);
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
        Mockito.verifyZeroInteractions(terminalServiceMock);
    }

    @Test
    public void shouldReturnANewTerminalIfAllFieldsAreValid() throws Exception {
        String body = "123";
        when(terminalStringValidatorServiceMock.performValidations(body)).thenReturn(new ValidationErrorDTO());

        Terminal terminal = new Terminal();
        terminal.setLogic(44332211);
        terminal.setSerial("123");
        terminal.setModel("PWWIN");
        terminal.setSam(0);
        terminal.setPtid("F04A2E4088B");
        terminal.setPlat(4);
        terminal.setVersion("8.00b3");
        terminal.setMxr(1);
        terminal.setMxf(16777216);
        terminal.setVerfm("PWWINV");

        when(terminalServiceMock.createTerminal(body)).thenReturn(terminal);

        MockHttpServletRequestBuilder builder = post(BASE_URL).contentType(MediaType.TEXT_HTML_VALUE).content(body);
        this.mockMvc.perform(builder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.logic", is(44332211)))
                .andExpect(jsonPath("$.serial", is("123")))
                .andExpect(jsonPath("$.model", is("PWWIN")))
                .andExpect(jsonPath("$.sam", is(0)))
                .andExpect(jsonPath("$.ptid", is("F04A2E4088B")))
                .andExpect(jsonPath("$.plat", is(4)))
                .andExpect(jsonPath("$.version", is("8.00b3")))
                .andExpect(jsonPath("$.mxr", is(1)))
                .andExpect(jsonPath("$.mxf", is(16777216)))
                .andExpect(jsonPath("$.verfm", is("PWWINV")));

        Mockito.verify(terminalStringValidatorServiceMock, only()).performValidations(body);
        Mockito.verify(terminalServiceMock, only()).createTerminal(body);
    }

    @Test
    public void shouldReturnNotFoundIfNoTerminalIsFoundWithGivenLogic() throws Exception {
        mockMvc.perform(get(BASE_URL + "/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnSavedTerminalWithGivenLogic() throws Exception {
        int logic = 44332211;
        Terminal savedTerminal = new Terminal();
        savedTerminal.setLogic(logic);
        savedTerminal.setSerial("123");
        savedTerminal.setModel("PWWIN");
        savedTerminal.setSam(0);
        savedTerminal.setPtid("F04A2E4088B");
        savedTerminal.setPlat(4);
        savedTerminal.setVersion("8.00b3");
        savedTerminal.setMxr(1);
        savedTerminal.setMxf(16777216);
        savedTerminal.setVerfm("PWWINV");

        when(terminalServiceMock.findTerminalByLogic(logic)).thenReturn(java.util.Optional.of(savedTerminal));

        mockMvc.perform(get(BASE_URL + "/" + logic))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.logic", is(logic)))
                .andExpect(jsonPath("$.serial", is("123")))
                .andExpect(jsonPath("$.model", is("PWWIN")))
                .andExpect(jsonPath("$.sam", is(0)))
                .andExpect(jsonPath("$.ptid", is("F04A2E4088B")))
                .andExpect(jsonPath("$.plat", is(4)))
                .andExpect(jsonPath("$.version", is("8.00b3")))
                .andExpect(jsonPath("$.mxr", is(1)))
                .andExpect(jsonPath("$.mxf", is(16777216)))
                .andExpect(jsonPath("$.verfm", is("PWWINV")));
    }

    @Test
    public void shouldReturnNotFoundWhenTryToUpdateATerminalThatDoesNotExist() throws Exception {
        HashMap<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("serial", "124");
        jsonMap.put("model", "PWWIN1");
        jsonMap.put("sam", 1);
        jsonMap.put("ptid", "F04A2E4088B1");
        jsonMap.put("plat", 5);
        jsonMap.put("version", "8.00b31");
        jsonMap.put("mxr", 12);
        jsonMap.put("mxf", 16777217);
        jsonMap.put("verfm", "PWWINV1");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(jsonMap);

        mockMvc.perform(put(BASE_URL + "/123").contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonString))
                .andExpect(status().isNotFound());
    }
}
