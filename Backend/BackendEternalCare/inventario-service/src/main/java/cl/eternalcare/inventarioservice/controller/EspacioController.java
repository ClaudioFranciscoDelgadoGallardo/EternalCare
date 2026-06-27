package cl.eternalcare.inventarioservice.controller;

import cl.eternalcare.inventarioservice.model.Espacio;
import cl.eternalcare.inventarioservice.dto.EspacioRequestDTO;
import cl.eternalcare.inventarioservice.service.EspacioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/espacios")
@RequiredArgsConstructor
public class EspacioController {

    private final EspacioService espacioService;

    @GetMapping
    public ResponseEntity<List<Espacio>> listarTodos() {
        return ResponseEntity.ok(espacioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Espacio> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(espacioService.obtenerPorId(id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crearEspacio(@RequestBody EspacioRequestDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(espacioService.registrarEspacio(dto));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/disponibles/{sector}")
    public ResponseEntity<List<Espacio>> listarDisponibles(@PathVariable String sector) {
        return ResponseEntity.ok(espacioService.listarDisponibles(sector));
    }

    @PostMapping("/{id}/reservar")
    public ResponseEntity<?> reservarEspacio(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(espacioService.reservarEspacio(id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
}