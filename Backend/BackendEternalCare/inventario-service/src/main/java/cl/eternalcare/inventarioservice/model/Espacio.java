package cl.eternalcare.inventarioservice.model;

import cl.eternalcare.inventarioservice.enums.EstadoEspacio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "espacios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Espacio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 80)
    private String sector;

    @Column(nullable = false, length = 60)
    private String tipo;

    @Column(nullable = false)
    private Integer capacidad = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEspacio estado;

    @Column
    private String georreferenciacion;

    @Column
    private String descripcion;
}