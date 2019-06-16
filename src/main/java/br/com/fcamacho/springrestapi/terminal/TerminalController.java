package br.com.fcamacho.springrestapi.terminal;

import br.com.fcamacho.springrestapi.genericValidation.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("v1/terminal")
public class TerminalController {

    private final TerminalStringValidatorService terminalStringValidatorService;
    private final TerminalService terminalService;

    public TerminalController(TerminalStringValidatorService terminalStringValidatorService, TerminalService terminalService) {
        this.terminalStringValidatorService = terminalStringValidatorService;
        this.terminalService = terminalService;
    }

    @GetMapping(path = "{logic}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity findTerminalByLogic(@PathVariable int logic) {
        Optional<Terminal> optionalTerminal = terminalService.findTerminalByLogic(logic);

        if (optionalTerminal.isPresent()) {
            return ResponseEntity.ok(optionalTerminal.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity createTerminal(@RequestBody String terminalString) {
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        if (validationErrorDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(validationErrorDTO);
        }

        Terminal terminal = terminalService.createTerminal(terminalString);
        return ResponseEntity.status(HttpStatus.CREATED).body(terminal);
    }
}
