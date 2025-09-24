package com.docelectronico.cliente.controller;

import java.time.LocalDate;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.docelectronico.cliente.entity.Cliente;
import com.docelectronico.cliente.service.ClienteService;

@RestController
@RequestMapping("api/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@PostMapping("/createCliente")
	public ResponseEntity<?> createCliente(@RequestBody Cliente cliente){
		try {
			Cliente clienteDtoResp = clienteService.createCliente(cliente);	
			return new ResponseEntity<>(clienteDtoResp, HttpStatus.OK);
		} catch (Exception e) {
			HashMap<String, String> responseBody = new HashMap<String, String>();
			String mensaje = e.getMessage();
			responseBody.put("timestamp", LocalDate.now().toString());
			responseBody.put("mensaje", mensaje);
			responseBody.put("estatus", "fallido");
			return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}

}
