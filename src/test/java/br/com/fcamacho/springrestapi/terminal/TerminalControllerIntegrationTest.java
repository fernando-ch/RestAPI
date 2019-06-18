package br.com.fcamacho.springrestapi.terminal;

import br.com.fcamacho.springrestapi.genericValidation.ValidationErrorDTO;
import br.com.fcamacho.springrestapi.testUtils.RestResponsePage;
import br.com.fcamacho.springrestapi.testUtils.SpringValidationDTO;
import br.com.fcamacho.springrestapi.testUtils.SpringValidationErrorDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TerminalControllerIntegrationTest {

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        baseUrl = "http://localhost:" + port + "/v1/terminal";
    }

    @Test
    public void shouldSaveANewTerminalInDataBase() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;1;16777216;PWWINV";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> request = new HttpEntity<>(terminalString, headers);

        // Verifying that the data from the request is correct
        Terminal terminalFromRequest = restTemplate.postForObject(baseUrl, request, Terminal.class);
        assertThat(terminalFromRequest.getLogic(), is(44332211));
        assertThat(terminalFromRequest.getSerial(), is("123"));
        assertThat(terminalFromRequest.getModel(), is("PWWIN"));
        assertThat(terminalFromRequest.getSam(), is(0));
        assertThat(terminalFromRequest.getPtid(), is("F04A2E4088B"));
        assertThat(terminalFromRequest.getPlat(), is(4));
        assertThat(terminalFromRequest.getVersion(), is("8.00b3"));
        assertThat(terminalFromRequest.getMxr(), is(1));
        assertThat(terminalFromRequest.getMxf(), is(16777216));
        assertThat(terminalFromRequest.getVerfm(), is("PWWINV"));

        // Verifying that the data from the database is correct
        Terminal terminalFromDatabase = terminalRepository.findByLogic(44332211).get();
        assertThat(terminalFromDatabase.getLogic(), is(44332211));
        assertThat(terminalFromDatabase.getSerial(), is("123"));
        assertThat(terminalFromDatabase.getModel(), is("PWWIN"));
        assertThat(terminalFromDatabase.getSam(), is(0));
        assertThat(terminalFromDatabase.getPtid(), is("F04A2E4088B"));
        assertThat(terminalFromDatabase.getPlat(), is(4));
        assertThat(terminalFromDatabase.getVersion(), is("8.00b3"));
        assertThat(terminalFromDatabase.getMxr(), is(1));
        assertThat(terminalFromDatabase.getMxf(), is(16777216));
        assertThat(terminalFromDatabase.getVerfm(), is("PWWINV"));
    }

    @Test
    public void shouldRejectPostIfLogicIsDuplicated() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;1;16777216;PWWINV";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> request = new HttpEntity<>(terminalString, headers);

        restTemplate.postForObject(baseUrl, request, Terminal.class);
        ValidationErrorDTO validationErrorDTO = restTemplate.postForObject(baseUrl, request, ValidationErrorDTO.class);

        assertTrue(validationErrorDTO.hasErrors());
        assertThat(validationErrorDTO.getErrors(), hasSize(0));
        assertThat(validationErrorDTO.getFieldErrors(), hasSize(1));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getField(), is("logic"));
        assertThat(validationErrorDTO.getFieldErrors().get(0).getMessage(), is("This property must be unique"));
    }

    @Test
    public void shouldReturnPreviousSavedTerminal() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;1;16777216;PWWINV";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> request = new HttpEntity<>(terminalString, headers);

        restTemplate.postForObject(baseUrl, request, Terminal.class);

        Terminal terminal = restTemplate.getForObject(baseUrl + "/44332211", Terminal.class);

        assertThat(terminal.getLogic(), is(44332211));
        assertThat(terminal.getSerial(), is("123"));
        assertThat(terminal.getModel(), is("PWWIN"));
        assertThat(terminal.getSam(), is(0));
        assertThat(terminal.getPtid(), is("F04A2E4088B"));
        assertThat(terminal.getPlat(), is(4));
        assertThat(terminal.getVersion(), is("8.00b3"));
        assertThat(terminal.getMxr(), is(1));
        assertThat(terminal.getMxf(), is(16777216));
        assertThat(terminal.getVerfm(), is("PWWINV"));
    }

    @Test
    public void shouldReturnAPaginatedResultSortedByLogicDesc() {
        String templateTerminalString = ";123;PWWIN;0;F04A2E4088B;4;8.00b3;1;16777216;PWWINV";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        for (int i = 1; i <= 10; i++) {
            String terminalString = i + templateTerminalString;
            HttpEntity<String> request = new HttpEntity<>(terminalString, headers);
            restTemplate.postForObject(baseUrl, request, Terminal.class);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("page", 1)
                .queryParam("size", 4)
                .queryParam("sort", "logic,desc");

        ParameterizedTypeReference<RestResponsePage<Terminal>> typeReference =
                new ParameterizedTypeReference<RestResponsePage<Terminal>>() {};

        ResponseEntity<RestResponsePage<Terminal>> responseEntity = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, null, typeReference);

        Page<Terminal> page = responseEntity.getBody();

        assertThat(page.getTotalElements(), is(10L));
        assertThat(page.getTotalPages(), is(3));
        assertThat(page.getNumberOfElements(), is(4));
        assertThat(page.getNumber(), is(1));
        assertThat(page.getContent().get(0).getLogic(), is(6));
        assertThat(page.getContent().get(1).getLogic(), is(5));
        assertThat(page.getContent().get(2).getLogic(), is(4));
        assertThat(page.getContent().get(3).getLogic(), is(3));
    }

    @Test
    public void shouldRejectPutIfHasInvalidFields() throws JsonProcessingException {
        int logic = 1;
        HashMap<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("sam", -1);
        jsonMap.put("ptid", "F04A2E4088B1");
        jsonMap.put("plat", 5);
        jsonMap.put("mxr", 12);
        jsonMap.put("mxf", 16777217);
        jsonMap.put("verfm", "PWWINV1");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(jsonMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        restTemplate.postForObject(baseUrl, request, Terminal.class);
        ResponseEntity<SpringValidationDTO> responseEntity = restTemplate.exchange(baseUrl + "/" + logic, HttpMethod.PUT,
                request, SpringValidationDTO.class);

        SpringValidationDTO validationDTO = responseEntity.getBody();

        validationDTO.getErrors().sort(Comparator.comparing(SpringValidationErrorDTO::getField));

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(validationDTO.getErrors(), hasSize(4));
        assertThat(validationDTO.getErrors().get(0).getField(), is("model"));
        assertThat(validationDTO.getErrors().get(0).getDefaultMessage(), is("must not be empty"));
        assertThat(validationDTO.getErrors().get(1).getField(), is("sam"));
        assertThat(validationDTO.getErrors().get(1).getDefaultMessage(), is("must be greater than or equal to 0"));
        assertThat(validationDTO.getErrors().get(2).getField(), is("serial"));
        assertThat(validationDTO.getErrors().get(2).getDefaultMessage(), is("must not be empty"));
        assertThat(validationDTO.getErrors().get(3).getField(), is("version"));
        assertThat(validationDTO.getErrors().get(3).getDefaultMessage(), is("must not be empty"));
    }

    @Test
    public void shouldUpdateAllFields() throws JsonProcessingException {
        int logic = 1;
        String terminalString = logic + ";123;PWWIN;0;F04A2E4088B;4;8.00b3;1;16777216;PWWINV";

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.TEXT_HTML);
        HttpEntity<String> postRequest = new HttpEntity<>(terminalString, postHeaders);
        restTemplate.postForObject(baseUrl, postRequest, Terminal.class);

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

        HttpHeaders putHeaders = new HttpHeaders();
        putHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> putRequest = new HttpEntity<>(jsonString, putHeaders);

        ResponseEntity responseEntity = restTemplate.exchange(baseUrl + "/" + logic, HttpMethod.PUT, putRequest, Object.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NO_CONTENT));

        Terminal terminal = terminalRepository.findByLogic(logic).get();

        assertThat(terminal.getLogic(), is(1));
        assertThat(terminal.getSerial(), is("124"));
        assertThat(terminal.getModel(), is("PWWIN1"));
        assertThat(terminal.getSam(), is(1));
        assertThat(terminal.getPtid(), is("F04A2E4088B1"));
        assertThat(terminal.getPlat(), is(5));
        assertThat(terminal.getVersion(), is("8.00b31"));
        assertThat(terminal.getMxr(), is(12));
        assertThat(terminal.getMxf(), is(16777217));
        assertThat(terminal.getVerfm(), is("PWWINV1"));
    }
}
