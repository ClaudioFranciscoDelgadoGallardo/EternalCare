package cl.eternalcare.contratosservice.service;

import java.util.List;

import cl.eternalcare.contratosservice.repository.ContratoRepository;
import cl.eternalcare.contratosservice.model.Contrato;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContratoService {

	private static final String ESTADO_ACTIVO = "ACTIVO";

	private final ContratoRepository contratoRepository;

	public List<Contrato> listarContratos() {
		return contratoRepository.findAll();
	}

	public Contrato obtenerContratoPorId(Long id) {
		return contratoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No existe un contrato con id: " + id));
	}

	@Transactional
	public Contrato registrarContrato(Contrato contrato) {
		if (contrato.getRutCliente() == null || contrato.getRutCliente().isBlank()) {
			throw new IllegalArgumentException("rutCliente es obligatorio");
		}

		if (contrato.getEspacioId() == null) {
			throw new IllegalArgumentException("espacioId es obligatorio");
		}

		if (contrato.getFechaInicio() == null) {
			throw new IllegalArgumentException("fechaInicio es obligatoria");
		}

		if (contrato.getMontoMensual() == null || contrato.getMontoMensual() <= 0) {
			throw new IllegalArgumentException("montoMensual debe ser mayor que 0");
		}

		contrato.setEstado(ESTADO_ACTIVO);
		return contratoRepository.save(contrato);
	}

	public List<Contrato> buscarContratosActivosPorCliente(String rutCliente) {
		return contratoRepository.findByRutCliente(rutCliente).stream()
				.filter(contrato -> ESTADO_ACTIVO.equalsIgnoreCase(contrato.getEstado()))
				.toList();
	}
}