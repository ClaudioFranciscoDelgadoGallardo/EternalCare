package cl.eternalcare.contratosservice;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contratos")
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String rutCliente;

	@Column(nullable = false)
	private Long espacioId;

	@Column(nullable = false)
	private LocalDate fechaInicio;

	@Column(nullable = false)
	private Double montoMensual;

	@Column(nullable = false)
	private String estado;
}