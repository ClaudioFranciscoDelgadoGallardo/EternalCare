package cl.eternalcare.contratosservice.repository;

import java.util.List;

import cl.eternalcare.contratosservice.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	List<Contrato> findByRutCliente(String rutCliente);
}