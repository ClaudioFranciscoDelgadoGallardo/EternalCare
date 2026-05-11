package cl.eternalcare.contratosservice;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContratoService {

	private static final String ESTADO_ACTIVO = "ACTIVO";

	private final ContratoRepository contratoRepository;

	public Contrato registrarContrato(Contrato contrato) {
		contrato.setEstado(ESTADO_ACTIVO);
		return contratoRepository.save(contrato);
	}

	public List<Contrato> buscarContratosActivosPorCliente(String rutCliente) {
		return contratoRepository.findByRutCliente(rutCliente).stream()
				.filter(contrato -> ESTADO_ACTIVO.equalsIgnoreCase(contrato.getEstado()))
				.toList();
	}
}