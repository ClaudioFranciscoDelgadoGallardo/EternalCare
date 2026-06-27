package cl.eternalcare.inventarioservice.dto;

import lombok.Data;

@Data
public class EspacioRequestDTO {
    private String codigo;
    private String sector;
    private String tipo;
    private Integer capacidad;
    private String georreferenciacion;
    private String descripcion;
}