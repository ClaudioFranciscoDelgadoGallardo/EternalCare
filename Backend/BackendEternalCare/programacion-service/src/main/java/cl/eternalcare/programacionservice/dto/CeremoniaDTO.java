package cl.eternalcare.programacionservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CeremoniaDTO {
    private String tipoCeremonia; // "FUNERAL", "CREMACION" o "VISITA"
    private Long clienteId;
    private Long contratoId;
    private Long espacioId;
    private LocalDateTime fechaHora;
    private String ubicacion;
    private UUID responsableUsuarioId;
    private String observacion;
}
