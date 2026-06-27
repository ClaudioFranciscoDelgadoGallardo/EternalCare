package cl.eternalcare.programacionservice.service;

import cl.eternalcare.programacionservice.dto.CeremoniaDTO;
import cl.eternalcare.programacionservice.enums.EstadoCeremonia;
import cl.eternalcare.programacionservice.factory.CeremoniaFactory;
import cl.eternalcare.programacionservice.model.Ceremonia;
import cl.eternalcare.programacionservice.repository.CeremoniaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramacionService {

    private final CeremoniaRepository repository;
    private final CeremoniaFactory factory;

    public List<Ceremonia> obtenerTodas() {
        return repository.findAll();
    }

    @Transactional
    public Ceremonia programarCeremonia(CeremoniaDTO dto) {
        Ceremonia nuevaCeremonia = factory.crearCeremonia(dto);
        nuevaCeremonia.setEstado(EstadoCeremonia.PROGRAMADA);
        return repository.save(nuevaCeremonia);
    }
}