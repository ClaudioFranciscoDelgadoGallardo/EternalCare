package cl.eternalcare.programacionservice.factory;

import cl.eternalcare.programacionservice.entity.*;
import java.time.LocalDateTime;

public class CeremoniaFactory {

    public static Ceremonia crearCeremonia(String tipo, LocalDateTime fechaHora, String ubicacion, String estado) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo de ceremonia no puede ser nulo o vacío");
        }

        switch (tipo.toUpperCase()) {
            case "FUNERAL":
                return new Funeral(fechaHora, ubicacion, estado);
            case "CREMACION":
                return new Cremacion(fechaHora, ubicacion, estado);
            case "VISITA":
                return new Visita(fechaHora, ubicacion, estado);
            default:
                throw new IllegalArgumentException("Tipo de ceremonia inválido: " + tipo + 
                    ". Los tipos válidos son: FUNERAL, CREMACION, VISITA");
        }
    }

}
