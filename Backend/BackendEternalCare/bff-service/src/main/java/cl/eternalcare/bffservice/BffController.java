package cl.eternalcare.bffservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	public ResponseEntity<?> obtenerTodosLosEspacios() {
		return restTemplate.exchange(
				inventarioBaseUrl + "/api/espacios",
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@GetMapping("/espacios/disponibles/{sector}")
	public ResponseEntity<?> obtenerEspaciosDisponibles(@PathVariable String sector) {
		return restTemplate.exchange(
				inventarioBaseUrl + "/api/espacios/disponibles/" + sector,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@PostMapping("/espacios")
	public ResponseEntity<?> crearEspacio(@RequestBody Object espacio) {
		return restTemplate.exchange(
				inventarioBaseUrl + "/api/espacios",
				HttpMethod.POST,
				new HttpEntity<>(espacio),
				Object.class
		);
	}

	@PostMapping("/espacios/{id}/reservar")
	public ResponseEntity<?> reservarEspacio(@PathVariable Long id) {
		return restTemplate.exchange(
				inventarioBaseUrl + "/api/espacios/" + id + "/reservar",
				HttpMethod.POST,
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

	@GetMapping("/contratos")
	public ResponseEntity<?> listarContratos() {
		return restTemplate.exchange(
				contratosBaseUrl + "/api/contratos",
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@GetMapping("/contratos/{id}")
	public ResponseEntity<?> obtenerContratoPorId(@PathVariable Long id) {
		return restTemplate.exchange(
				contratosBaseUrl + "/api/contratos/" + id,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@GetMapping("/contratos/cliente/{rut}")
	public ResponseEntity<?> obtenerContratosActivosPorCliente(@PathVariable String rut) {
		return restTemplate.exchange(
				contratosBaseUrl + "/api/contratos/cliente/" + rut,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@PostMapping("/ceremonias")
	public ResponseEntity<?> agendarCeremonia(@RequestBody Object ceremonia) {
		return restTemplate.exchange(
				programacionBaseUrl + "/api/ceremonias",
				HttpMethod.POST,
				new HttpEntity<>(ceremonia),
				Object.class
		);
	}

	@GetMapping("/ceremonias")
	public ResponseEntity<?> listarCeremonias() {
		return restTemplate.exchange(
				programacionBaseUrl + "/api/ceremonias",
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@GetMapping("/ceremonias/{id}")
	public ResponseEntity<?> obtenerCeremoniaPorId(@PathVariable Long id) {
		return restTemplate.exchange(
				programacionBaseUrl + "/api/ceremonias/" + id,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@PostMapping("/ceremonias/{id}/eliminar")
	public ResponseEntity<?> eliminarCeremonia(@PathVariable Long id) {
		return restTemplate.exchange(
				programacionBaseUrl + "/api/ceremonias/" + id,
				HttpMethod.DELETE,
				HttpEntity.EMPTY,
				Object.class
		);
	}

	@DeleteMapping("/ceremonias/{id}")
	public ResponseEntity<?> eliminarCeremoniaDelete(@PathVariable Long id) {
		return restTemplate.exchange(
				programacionBaseUrl + "/api/ceremonias/" + id,
				HttpMethod.DELETE,
				HttpEntity.EMPTY,
				Object.class
		);
	}
}