package cl.eternalcare.programacionservice.model;

import cl.eternalcare.programacionservice.enums.TipoCeremonia;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FUNERAL")
public class Funeral extends Ceremonia {
    @Override
    public TipoCeremonia getTipo() { return TipoCeremonia.FUNERAL; }
}
