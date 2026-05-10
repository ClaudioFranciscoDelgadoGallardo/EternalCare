package cl.eternalcare.programacionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CeremoniaDTO {

    private String tipo;
    private LocalDateTime fechaHora;
    private String ubicacion;
    private String estado;

}
