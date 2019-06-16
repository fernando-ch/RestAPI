package br.com.fcamacho.springrestapi.terminal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
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
        Terminal terminalFromDatabase = terminalRepository.findById(44332211).get();
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

}
