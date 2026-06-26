package cl.eternalcare.programacionservice.service;

import cl.eternalcare.programacionservice.entity.Ceremonia;
import cl.eternalcare.programacionservice.factory.CeremoniaFactory;
import cl.eternalcare.programacionservice.repository.CeremoniaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramacionService {

    private final CeremoniaRepository ceremoniasRepository;

    public Ceremonia agendar(String tipo, LocalDateTime fechaHora, String ubicacion, String estado) {
        Ceremonia ceremonia = CeremoniaFactory.crearCeremonia(tipo, fechaHora, ubicacion, estado);
        return ceremoniasRepository.save(ceremonia);
    }

    public List<Ceremonia> obtenerPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return obtenerTodas();
        }

        return ceremoniasRepository.findAll().stream()
            .filter(ceremonia -> ceremonia.getClass().getSimpleName().equalsIgnoreCase(tipo))
            .toList();
    }

    public List<Ceremonia> obtenerTodas() {
        return ceremoniasRepository.findAll();
    }

    public Ceremonia obtenerPorId(Long id) {
        return ceremoniasRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Ceremonia no encontrada con ID: " + id));
    }

    public void eliminar(Long id) {
        if (!ceremoniasRepository.existsById(id)) {
            throw new IllegalArgumentException("Ceremonia no encontrada con ID: " + id);
        }
        ceremoniasRepository.deleteById(id);
    }

}
