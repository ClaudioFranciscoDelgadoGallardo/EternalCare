package cl.eternalcare.inventarioservice.servicio;

import cl.eternalcare.inventarioservice.entidad.Espacio;
import cl.eternalcare.inventarioservice.repository.EspacioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EspacioService {

    private final EspacioRepository espacioRepository;

    public List<Espacio> listarTodos() {
        return espacioRepository.findAll();
    }

    public Espacio obtenerPorId(Long id) {
        return espacioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un espacio con id: " + id));
    }

    @Transactional
    public Espacio registrarEspacio(Espacio espacio) {
        if (espacio.getSector() == null || espacio.getSector().isBlank()) {
            throw new IllegalArgumentException("sector es obligatorio");
        }

        if (espacio.getTipo() == null || espacio.getTipo().isBlank()) {
            throw new IllegalArgumentException("tipo es obligatorio");
        }

        if (espacio.getEstado() == null || espacio.getEstado().isBlank()) {
            espacio.setEstado("disponible");
        }

        return espacioRepository.save(espacio);
    }

    public List<Espacio> listarDisponibles(String sector) {
        return espacioRepository.findBySectorAndEstado(sector, "disponible");
    }

    @Transactional
    public Espacio reservarEspacio(Long id) {
        Espacio espacio = espacioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un espacio con id: " + id));

        if (!"disponible".equalsIgnoreCase(espacio.getEstado())) {
            throw new IllegalStateException("El espacio ya está ocupado y no puede reservarse nuevamente");
        }

        espacio.setEstado("ocupado");
        return espacioRepository.save(espacio);
    }
}
