package com.docelectronico.cliente.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docelectronico.cliente.dao.ClienteRepository;
import com.docelectronico.cliente.entity.Cliente;
import com.docelectronico.cliente.service.ClienteService;
@Service
public class ClienteServiceImpl implements ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public Cliente createCliente(Cliente cliente) {		
		return clienteRepository.save(cliente);
	}

}
