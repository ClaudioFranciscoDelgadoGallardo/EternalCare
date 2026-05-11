package cl.eternalcare.bffservice;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bff")
@RequiredArgsConstructor
public class BffController {

	private final RestTemplate restTemplate;

	@Value("${services.inventario.base-url}")
	private String inventarioBaseUrl;

	@Value("${services.contratos.base-url}")
	private String contratosBaseUrl;

	@GetMapping("/inventario/disponibles/{sector}")
	public ResponseEntity<?> obtenerEspaciosDisponibles(@PathVariable String sector) {
		return restTemplate.exchange(
				inventarioBaseUrl + "/disponibles/{sector}",
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class,
				sector
		);
	}

	@PostMapping("/contratos")
	public ResponseEntity<?> crearContrato(@RequestBody Map<String, Object> contrato) {
		return restTemplate.exchange(
				contratosBaseUrl,
				HttpMethod.POST,
				new HttpEntity<>(contrato),
				Object.class
		);
	}
}