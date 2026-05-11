package cl.eternalcare.contratosservice;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
public class ContratoController {

	private final ContratoService contratoService;

	@PostMapping
	public ResponseEntity<Contrato> crearContrato(@RequestBody Contrato contrato) {
		Contrato contratoCreado = contratoService.registrarContrato(contrato);
		return ResponseEntity.status(HttpStatus.CREATED).body(contratoCreado);
	}

	@GetMapping("/cliente/{rut}")
	public ResponseEntity<List<Contrato>> buscarContratosActivosPorCliente(@PathVariable("rut") String rutCliente) {
		List<Contrato> contratos = contratoService.buscarContratosActivosPorCliente(rutCliente);
		return ResponseEntity.ok(contratos);
	}
}