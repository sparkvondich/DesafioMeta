package com.example.clientmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.clientmanagement.dto.ClienteMapper;
import com.example.clientmanagement.dto.ClienteRequestDTO;
import com.example.clientmanagement.dto.EnderecoRequestDTO;
import com.example.clientmanagement.entity.Cliente;
import com.example.clientmanagement.entity.Endereco;
import com.example.clientmanagement.exception.ResourceNotFoundException;
import com.example.clientmanagement.integration.ViaCepService;
import com.example.clientmanagement.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;
	private final ViaCepService viaCepService;

	@Autowired
	public ClienteService(ClienteRepository clienteRepository, ViaCepService viaCepService) {
		this.clienteRepository = clienteRepository;
		this.viaCepService = viaCepService;
	}

	public Cliente cadastrarCliente(ClienteRequestDTO clienteRequestDTO) {
		// Mapeia DTO para entidade
		Cliente cliente = ClienteMapper.toEntity(clienteRequestDTO);

		// Popular endere�os com dados do ViaCep
		List<Endereco> enderecosPopulados = viaCepService.popularEnderecos(clienteRequestDTO.getEnderecos(), cliente);
		cliente.setEnderecos(enderecosPopulados);

		// Salvar o cliente e seus endere�os no banco de dados
		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente atualizarCliente(Long clienteId, List<EnderecoRequestDTO> enderecosDTO) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente n�o encontrado"));

		List<Endereco> enderecos = new ArrayList<>();

		for (EnderecoRequestDTO enderecoDTO : enderecosDTO) {
			Endereco endereco = consultarEndereco(enderecoDTO, cliente);
			enderecos.add(endereco);
		}

		// Atualiza a cole��o de endere�os
		cliente.getEnderecos().clear(); // Limpa os endere�os existentes
		cliente.getEnderecos().addAll(enderecos); // Adiciona os novos endere�os

		return clienteRepository.save(cliente);
	}
	
	private Endereco consultarEndereco(EnderecoRequestDTO enderecoDTO, Cliente cliente) {
	    // Consulta o endere�o usando o servi�o ViaCep
	    Endereco endereco = viaCepService.consultarEndereco(enderecoDTO, cliente);

	    // Se o endere�o retornado for nulo ou vazio, voc� pode lan�ar uma exce��o ou lidar com a l�gica conforme necess�rio.
	    if (endereco == null) {
	        throw new ResourceNotFoundException("Endere�o n�o encontrado para o CEP: " + enderecoDTO.getCep());
	    }

	    // Define o cliente para o endere�o consultado
	    endereco.setCliente(cliente);

	    return endereco;
	}


	public Optional<Cliente> obterCliente(Long id) {
		return clienteRepository.findById(id);
	}

	public void excluirCliente(Long id) {
		clienteRepository.deleteById(id);
	}
}
