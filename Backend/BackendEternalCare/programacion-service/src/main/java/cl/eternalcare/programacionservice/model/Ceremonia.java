package cl.eternalcare.programacionservice.model;

import cl.eternalcare.programacionservice.enums.EstadoCeremonia;
import cl.eternalcare.programacionservice.enums.TipoCeremonia;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ceremonias")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ceremonia", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public abstract class Ceremonia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "contrato_id")
    private Long contratoId;

    @Column(name = "espacio_id")
    private Long espacioId;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 150)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCeremonia estado;

    @Column(name = "responsable_usuario_id")
    private UUID responsableUsuarioId;

    @Column
    private String observacion;

    // Método abstracto para que cada hija implemente su lógica si es necesario
    public abstract TipoCeremonia getTipo();
}