package br.com.fcamacho.springrestapi.terminal;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class TerminalService {

    private final TerminalRepository terminalRepository;

    public TerminalService(TerminalRepository terminalRepository) {
        this.terminalRepository = terminalRepository;
    }

    public Optional<Terminal> findTerminalByLogic(int logic) {
        return terminalRepository.findByLogic(logic);
    }

    public Terminal createTerminal(String terminalString) {
        String[] properties = StringUtils.delimitedListToStringArray(terminalString, ";");
        Terminal terminal = new Terminal();

        terminal.setLogic(toInteger(properties[0]));
        terminal.setSerial(properties[1]);
        terminal.setModel(properties[2]);
        terminal.setSam(toInteger(properties[3]));
        terminal.setPtid(properties[4]);
        terminal.setPlat(toInteger(properties[5]));
        terminal.setVersion(properties[6]);
        terminal.setMxr(toInteger(properties[7]));
        terminal.setMxf(toInteger(properties[8]));
        terminal.setVerfm(properties[9]);

        terminalRepository.save(terminal);

        return terminal;
    }

    private Integer toInteger(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        return Integer.valueOf(s);
    }

    public boolean terminalExists(Integer logic) {
        return terminalRepository.existsByLogic(logic);
    }
}
