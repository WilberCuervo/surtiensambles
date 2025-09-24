package com.surtiensambes.commos.cliente.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacto {
	@JsonInclude(Include.NON_NULL)
	private String nombre;
	@JsonInclude(Include.NON_NULL)
	private String telefono;
	@JsonInclude(Include.NON_NULL)
	private String correo;
	@JsonInclude(Include.NON_NULL)
	private String nota;
	@JsonInclude(Include.NON_NULL)
	private String celular;
}
