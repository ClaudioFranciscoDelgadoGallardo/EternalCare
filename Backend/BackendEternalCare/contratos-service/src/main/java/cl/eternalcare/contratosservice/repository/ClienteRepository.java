package cl.eternalcare.contratosservice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.eternalcare.contratosservice.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Método mágico de Spring Data para buscar por RUT
    Optional<Cliente> findByRut(String rut);
}