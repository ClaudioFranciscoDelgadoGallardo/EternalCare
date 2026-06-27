package cl.eternalcare.programacionservice.factory;

import cl.eternalcare.programacionservice.model.Ceremonia;
import cl.eternalcare.programacionservice.model.Cremacion;
import cl.eternalcare.programacionservice.model.Funeral;
import cl.eternalcare.programacionservice.model.Visita;
import cl.eternalcare.programacionservice.dto.CeremoniaDTO;
import org.springframework.stereotype.Component;

@Component
public class CeremoniaFactory {
    public Ceremonia crearCeremonia(CeremoniaDTO dto) {
        Ceremonia ceremonia;
        switch (dto.getTipoCeremonia().toUpperCase()) {
            case "FUNERAL":
                ceremonia = new Funeral();
                break;
            case "CREMACION":
                ceremonia = new Cremacion();
                break;
            case "VISITA":
                ceremonia = new Visita();
                break;
            default:
                throw new IllegalArgumentException("Tipo de ceremonia no válido: " + dto.getTipoCeremonia());
        }
        // Poblar datos comunes
        ceremonia.setClienteId(dto.getClienteId());
        ceremonia.setContratoId(dto.getContratoId());
        ceremonia.setEspacioId(dto.getEspacioId());
        ceremonia.setFechaHora(dto.getFechaHora());
        ceremonia.setUbicacion(dto.getUbicacion());
        ceremonia.setResponsableUsuarioId(dto.getResponsableUsuarioId());
        ceremonia.setObservacion(dto.getObservacion());
        return ceremonia;
    }
}