package cl.eternalcare.contratosservice.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.eternalcare.contratosservice.model.PerfilUsuario;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, UUID> {
    // Aquí podremos agregar búsquedas por rol si las necesitamos después
}