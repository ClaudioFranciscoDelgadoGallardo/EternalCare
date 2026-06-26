package cl.eternalcare.contratosservice.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.eternalcare.contratosservice.dto.ClienteRequestDTO;
import cl.eternalcare.contratosservice.enums.EstadoCliente;
import cl.eternalcare.contratosservice.model.Cliente;
import cl.eternalcare.contratosservice.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerClientePorRut(String rut) {
        return clienteRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("No existe un cliente con el RUT: " + rut));
    }

    @Transactional
    public Cliente registrarCliente(ClienteRequestDTO dto) {
        if (dto.getRut() == null || dto.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es obligatorio");
        }

        if (clienteRepository.findByRut(dto.getRut()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con este RUT");
        }

        // Transformamos el DTO a la Entidad real
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setRut(dto.getRut());
        nuevoCliente.setNombres(dto.getNombres());
        nuevoCliente.setApellidos(dto.getApellidos());
        nuevoCliente.setTelefono(dto.getTelefono());
        nuevoCliente.setEmail(dto.getEmail());
        nuevoCliente.setDireccion(dto.getDireccion());
        nuevoCliente.setEstado(EstadoCliente.activo); // Estado por defecto

        return clienteRepository.save(nuevoCliente);
    }
}