package cl.eternalcare.contratosservice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

	List<Contrato> findByRutCliente(String rutCliente);
}