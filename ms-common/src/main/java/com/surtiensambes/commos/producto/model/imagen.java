package com.surtiensambes.commos.producto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class imagen {
	private String minatura;
	private List<String> galeria;
	

}
