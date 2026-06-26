package cl.eternalcare.contratosservice.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cl.eternalcare.contratosservice.enums.RolUsuario;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "perfiles_usuarios")
public class PerfilUsuario {

    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nombres;

    @Column(nullable = false, length = 120)
    private String apellidos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol;

    @Column(nullable = false)
    private Boolean activo;
}
