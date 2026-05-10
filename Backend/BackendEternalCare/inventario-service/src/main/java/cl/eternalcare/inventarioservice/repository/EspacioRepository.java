package cl.eternalcare.inventarioservice.repository;

import cl.eternalcare.inventarioservice.entidad.Espacio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspacioRepository extends JpaRepository<Espacio, Long> {

    List<Espacio> findBySectorAndEstado(String sector, String estado);
}
