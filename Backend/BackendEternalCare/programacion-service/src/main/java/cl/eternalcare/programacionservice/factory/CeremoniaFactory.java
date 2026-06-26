package cl.eternalcare.programacionservice.factory;

import cl.eternalcare.programacionservice.entity.*;
import java.time.LocalDateTime;

public class CeremoniaFactory {

    public static Ceremonia crearCeremonia(String tipo, LocalDateTime fechaHora, String ubicacion, String estado) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo de ceremonia no puede ser nulo o vacío");
        }

        Ceremonia ceremonia;

        switch (tipo.toUpperCase()) {
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
                throw new IllegalArgumentException("Tipo de ceremonia inválido: " + tipo + 
                    ". Los tipos válidos son: FUNERAL, CREMACION, VISITA");
        }

        ceremonia.setFechaHora(fechaHora);
        ceremonia.setUbicacion(ubicacion);
        ceremonia.setEstado(estado == null || estado.isBlank() ? "PROGRAMADA" : estado);

        return ceremonia;
    }

}
