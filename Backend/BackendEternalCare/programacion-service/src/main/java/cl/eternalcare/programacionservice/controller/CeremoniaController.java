package cl.eternalcare.programacionservice.controller;

import cl.eternalcare.programacionservice.dto.CeremoniaDTO;
import cl.eternalcare.programacionservice.model.Ceremonia;
import cl.eternalcare.programacionservice.service.ProgramacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ceremonias")
@RequiredArgsConstructor
public class CeremoniaController {

    private final ProgramacionService programacionService;

    @GetMapping
    public ResponseEntity<List<Ceremonia>> listarTodas() {
        return ResponseEntity.ok(programacionService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<?> programarCeremonia(@RequestBody CeremoniaDTO dto) {
        try {
            Ceremonia agendada = programacionService.programarCeremonia(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(agendada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}