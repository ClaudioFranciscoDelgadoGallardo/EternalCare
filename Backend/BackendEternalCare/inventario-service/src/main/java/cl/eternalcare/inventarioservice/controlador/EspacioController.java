package cl.eternalcare.inventarioservice.controlador;

import cl.eternalcare.inventarioservice.entidad.Espacio;
import cl.eternalcare.inventarioservice.servicio.EspacioService;
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
    public ResponseEntity<Espacio> crearEspacio(@RequestBody Espacio espacio) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(espacioService.registrarEspacio(espacio));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/disponibles/{sector}")
    public ResponseEntity<List<Espacio>> listarDisponibles(@PathVariable String sector) {
        return ResponseEntity.ok(espacioService.listarDisponibles(sector));
    }

    @PostMapping("/{id}/reservar")
    public ResponseEntity<Espacio> reservarEspacio(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(espacioService.reservarEspacio(id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
