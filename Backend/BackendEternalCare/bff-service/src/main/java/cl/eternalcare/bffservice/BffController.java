package cl.eternalcare.bffservice;

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

	@Value("${app.inventario.url}")
	private String inventarioBaseUrl;

	@Value("${app.programacion.url}")
	private String programacionBaseUrl;

	@Value("${app.contratos.url}")
	private String contratosBaseUrl;

	@GetMapping("/espacios")
	public ResponseEntity<?> obtenerEspaciosDisponibles() {
		return restTemplate.exchange(
				inventarioBaseUrl + "/api/espacios",
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@PostMapping("/contratos")
	public ResponseEntity<?> crearContrato(@RequestBody Object contrato) {
		return restTemplate.exchange(
				contratosBaseUrl + "/api/contratos",
				HttpMethod.POST,
				new HttpEntity<>(contrato),
				Object.class
		);
	}
}