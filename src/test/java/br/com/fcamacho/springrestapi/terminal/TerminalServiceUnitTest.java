package br.com.fcamacho.springrestapi.terminal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class TerminalServiceUnitTest {

    @InjectMocks
    private TerminalService terminalService;

    @Mock
    private TerminalRepository terminalRepositoryMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnAnSavedTerminal() {
        int logic = 44332211;

        Terminal savedTerminal = new Terminal();
        savedTerminal.setLogic(44332211);
        savedTerminal.setSerial("123");
        savedTerminal.setModel("PWWIN");
        savedTerminal.setSam(0);
        savedTerminal.setPtid("F04A2E4088B");
        savedTerminal.setPlat(4);
        savedTerminal.setVersion("8.00b3");
        savedTerminal.setMxr(1);
        savedTerminal.setMxf(16777216);
        savedTerminal.setVerfm("PWWINV");

        when(terminalRepositoryMock.findByLogic(logic)).thenReturn(Optional.of(savedTerminal));

        Terminal terminal = terminalService.findTerminalByLogic(logic).get();

        assertThat(terminal, samePropertyValuesAs(savedTerminal));
        verify(terminalRepositoryMock, only()).findByLogic(logic);
    }

    @Test
    public void shouldReturnANewTerminalWithAllFields() {
        String terminalString = "44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;1;16777216;PWWINV";
        Terminal terminal = terminalService.createTerminal(terminalString);

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

        Mockito.verify(terminalRepositoryMock, only()).save(terminal);
    }

    @Test
    public void shouldReturnANewTerminalWithOnlyTheRequiredFields() {
        String terminalString = "44332211;123;PWWIN;;;;8.00b3;;;";
        Terminal terminal = terminalService.createTerminal(terminalString);

        assertThat(terminal.getLogic(), is(44332211));
        assertThat(terminal.getSerial(), is("123"));
        assertThat(terminal.getModel(), is("PWWIN"));
        assertThat(terminal.getSam(), nullValue());
        assertThat(terminal.getPtid(), nullValue());
        assertThat(terminal.getPlat(), nullValue());
        assertThat(terminal.getVersion(), is("8.00b3"));
        assertThat(terminal.getMxr(), nullValue());
        assertThat(terminal.getMxf(), nullValue());
        assertThat(terminal.getVerfm(), nullValue());

        Mockito.verify(terminalRepositoryMock, only()).save(terminal);
    }
}
