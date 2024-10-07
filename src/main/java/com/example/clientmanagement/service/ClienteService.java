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

		// Popular endereços com dados do ViaCep
		List<Endereco> enderecosPopulados = viaCepService.popularEnderecos(clienteRequestDTO.getEnderecos(), cliente);
		cliente.setEnderecos(enderecosPopulados);

		// Salvar o cliente e seus endereços no banco de dados
		return clienteRepository.save(cliente);
	}

	@Transactional
	public Cliente atualizarCliente(Long clienteId, List<EnderecoRequestDTO> enderecosDTO) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

		List<Endereco> enderecos = new ArrayList<>();

		for (EnderecoRequestDTO enderecoDTO : enderecosDTO) {
			Endereco endereco = consultarEndereco(enderecoDTO, cliente);
			enderecos.add(endereco);
		}

		// Atualiza a coleção de endereços
		cliente.getEnderecos().clear(); // Limpa os endereços existentes
		cliente.getEnderecos().addAll(enderecos); // Adiciona os novos endereços

		return clienteRepository.save(cliente);
	}
	
	private Endereco consultarEndereco(EnderecoRequestDTO enderecoDTO, Cliente cliente) {
	    // Consulta o endereço usando o serviço ViaCep
	    Endereco endereco = viaCepService.consultarEndereco(enderecoDTO, cliente);

	    // Se o endereço retornado for nulo ou vazio, você pode lançar uma exceção ou lidar com a lógica conforme necessário.
	    if (endereco == null) {
	        throw new ResourceNotFoundException("Endereço não encontrado para o CEP: " + enderecoDTO.getCep());
	    }

	    // Define o cliente para o endereço consultado
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
