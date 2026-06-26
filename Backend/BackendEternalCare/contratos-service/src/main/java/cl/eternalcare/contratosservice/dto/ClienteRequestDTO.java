package cl.eternalcare.contratosservice.dto;

import lombok.Data;

@Data
public class ClienteRequestDTO {
    // Solo pedimos los datos que el usuario o trabajador realmente puede ingresar
    private String rut;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;
    private String direccion;
}