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
            return ResponseEntity.status(HttpStatus.CREATED).body(ceremonia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Ceremonia>> obtenerTodasLasCeremonias() {
        List<Ceremonia> ceremonias = programacionService.obtenerTodas();
        return ResponseEntity.ok(ceremonias);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Ceremonia>> obtenerCeremoniasPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(programacionService.obtenerPorTipo(tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ceremonia> obtenerCeremoniaPorId(@PathVariable Long id) {
        try {
            Ceremonia ceremonia = programacionService.obtenerPorId(id);
            return ResponseEntity.ok(ceremonia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCeremonia(@PathVariable Long id) {
        try {
            programacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
