package br.com.fcamacho.springrestapi.terminal;

import br.com.fcamacho.springrestapi.genericValidation.ValidationErrorDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/terminal")
public class TerminalController {

    private final TerminalStringValidatorService terminalStringValidatorService;

    public TerminalController(TerminalStringValidatorService terminalStringValidatorService) {
        this.terminalStringValidatorService = terminalStringValidatorService;
    }

    @PostMapping(consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity createTerminal(@RequestBody String terminalString) {
        ValidationErrorDTO validationErrorDTO = terminalStringValidatorService.performValidations(terminalString);

        if (validationErrorDTO.hasErrors()) {
            return ResponseEntity.badRequest().body(validationErrorDTO);
        }

        return ResponseEntity.ok().build();
    }
}
