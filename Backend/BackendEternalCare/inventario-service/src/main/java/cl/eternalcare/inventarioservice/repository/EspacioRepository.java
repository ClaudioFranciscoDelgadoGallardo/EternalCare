package cl.eternalcare.inventarioservice.repository;

import cl.eternalcare.inventarioservice.model.Espacio;
import cl.eternalcare.inventarioservice.enums.EstadoEspacio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspacioRepository extends JpaRepository<Espacio, Long> {
    List<Espacio> findBySectorAndEstado(String sector, EstadoEspacio estado);
}