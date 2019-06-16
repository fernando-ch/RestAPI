package br.com.fcamacho.springrestapi.terminal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.only;

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
