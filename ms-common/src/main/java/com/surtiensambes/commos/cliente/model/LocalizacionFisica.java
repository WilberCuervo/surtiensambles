package com.surtiensambes.commos.cliente.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalizacionFisica {
	private Integer estratoInmueble;
	private String tipoUsoInmueble;
	private String codigoDaneMunicipio;
	private String ciudad;
	private String codigoPostal;
	private String departamento;
	private String codigoDaneDepartamento;
	private String pais;
	private String identificadorPais;
	private String lenguaje;
	private ArrayList<String> direccion;
	private String nota;

}
