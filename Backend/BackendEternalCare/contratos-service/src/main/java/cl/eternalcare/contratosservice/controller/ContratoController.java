package cl.eternalcare.contratosservice.controller;

import java.util.List;

import cl.eternalcare.contratosservice.model.Contrato;
import cl.eternalcare.contratosservice.service.ContratoService;
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

	@GetMapping
	public ResponseEntity<List<Contrato>> listarContratos() {
		return ResponseEntity.ok(contratoService.listarContratos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contrato> obtenerContratoPorId(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(contratoService.obtenerContratoPorId(id));
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<Contrato> crearContrato(@RequestBody Contrato contrato) {
		try {
			Contrato contratoCreado = contratoService.registrarContrato(contrato);
			return ResponseEntity.status(HttpStatus.CREATED).body(contratoCreado);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/cliente/{rut}")
	public ResponseEntity<List<Contrato>> buscarContratosActivosPorCliente(@PathVariable("rut") String rutCliente) {
		List<Contrato> contratos = contratoService.buscarContratosActivosPorCliente(rutCliente);
		return ResponseEntity.ok(contratos);
	}
}