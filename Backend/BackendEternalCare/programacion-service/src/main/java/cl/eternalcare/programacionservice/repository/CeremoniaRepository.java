package cl.eternalcare.programacionservice.repository;
import cl.eternalcare.programacionservice.model.Ceremonia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeremoniaRepository extends JpaRepository<Ceremonia, Long> {
}
