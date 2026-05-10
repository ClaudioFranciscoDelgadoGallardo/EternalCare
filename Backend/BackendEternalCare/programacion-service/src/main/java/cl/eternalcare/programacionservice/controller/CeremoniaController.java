package cl.eternalcare.programacionservice.controller;

import cl.eternalcare.programacionservice.dto.CeremoniaDTO;
import cl.eternalcare.programacionservice.entity.Ceremonia;
import cl.eternalcare.programacionservice.service.ProgramacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ceremonias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class CeremoniaController {

    private final ProgramacionService programacionService;

    @PostMapping
    public ResponseEntity<Ceremonia> agendarCeremonia(@RequestBody CeremoniaDTO dto) {
        try {
            Ceremonia ceremonia = programacionService.agendar(
                dto.getTipo(),
                dto.getFechaHora(),
                dto.getUbicacion(),
                dto.getEstado()
            );
            return new ResponseEntity<>(ceremonia, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Ceremonia>> obtenerTodasLasCeremonias() {
        List<Ceremonia> ceremonias = programacionService.obtenerTodas();
        return new ResponseEntity<>(ceremonias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ceremonia> obtenerCeremoniaPorId(@PathVariable Long id) {
        try {
            Ceremonia ceremonia = programacionService.obtenerPorId(id);
            return new ResponseEntity<>(ceremonia, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCeremonia(@PathVariable Long id) {
        try {
            programacionService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
