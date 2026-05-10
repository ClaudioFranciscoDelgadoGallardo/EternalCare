package cl.eternalcare.programacionservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("VISITA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Visita extends Ceremonia {

    public Visita(LocalDateTime fechaHora, String ubicacion, String estado) {
        super(null, fechaHora, ubicacion, estado);
    }

}
