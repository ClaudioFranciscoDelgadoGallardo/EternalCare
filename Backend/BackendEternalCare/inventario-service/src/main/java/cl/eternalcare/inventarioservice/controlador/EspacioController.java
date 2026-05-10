package cl.eternalcare.inventarioservice.controlador;

import cl.eternalcare.inventarioservice.entidad.Espacio;
import cl.eternalcare.inventarioservice.servicio.EspacioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/espacios")
@RequiredArgsConstructor
public class EspacioController {

    private final EspacioService espacioService;

    @GetMapping("/disponibles/{sector}")
    public List<Espacio> listarDisponibles(@PathVariable String sector) {
        return espacioService.listarDisponibles(sector);
    }

    @PostMapping("/{id}/reservar")
    public Espacio reservarEspacio(@PathVariable Long id) {
        return espacioService.reservarEspacio(id);
    }
}
