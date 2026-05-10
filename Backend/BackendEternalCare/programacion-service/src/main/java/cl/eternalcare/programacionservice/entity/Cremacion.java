package cl.eternalcare.programacionservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("CREMACION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cremacion extends Ceremonia {

    public Cremacion(LocalDateTime fechaHora, String ubicacion, String estado) {
        super(null, fechaHora, ubicacion, estado);
    }

}
