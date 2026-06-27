package cl.eternalcare.inventarioservice.service;

import cl.eternalcare.inventarioservice.model.Espacio;
import cl.eternalcare.inventarioservice.dto.EspacioRequestDTO;
import cl.eternalcare.inventarioservice.enums.EstadoEspacio;
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
    public Espacio registrarEspacio(EspacioRequestDTO dto) {
        if (dto.getSector() == null || dto.getSector().isBlank()) {
            throw new IllegalArgumentException("El sector es obligatorio");
        }
        if (dto.getTipo() == null || dto.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }

        Espacio espacio = new Espacio();
        espacio.setCodigo(dto.getCodigo());
        espacio.setSector(dto.getSector());
        espacio.setTipo(dto.getTipo());
        espacio.setCapacidad(dto.getCapacidad() != null ? dto.getCapacidad() : 1);
        espacio.setGeorreferenciacion(dto.getGeorreferenciacion());
        espacio.setDescripcion(dto.getDescripcion());
        espacio.setEstado(EstadoEspacio.disponible); // Nace disponible por defecto

        return espacioRepository.save(espacio);
    }

    public List<Espacio> listarDisponibles(String sector) {
        return espacioRepository.findBySectorAndEstado(sector, EstadoEspacio.disponible);
    }

    @Transactional
    public Espacio reservarEspacio(Long id) {
        Espacio espacio = obtenerPorId(id);

        if (!EstadoEspacio.disponible.equals(espacio.getEstado())) {
            throw new IllegalStateException("El espacio no está disponible para reserva");
        }

        espacio.setEstado(EstadoEspacio.ocupado);
        return espacioRepository.save(espacio);
    }
}