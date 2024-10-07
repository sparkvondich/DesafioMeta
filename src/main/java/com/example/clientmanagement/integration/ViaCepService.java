package com.example.clientmanagement.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.clientmanagement.dto.EnderecoRequestDTO;
import com.example.clientmanagement.entity.Cliente;
import com.example.clientmanagement.entity.Endereco;

@Service
public class ViaCepService {

	private final RestTemplate restTemplate;
	private static final Logger logger = LoggerFactory.getLogger(ViaCepService.class);

	@Autowired
	public ViaCepService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	
	public List<Endereco> popularEnderecos(List<EnderecoRequestDTO> enderecosDTO, Cliente cliente) {
	    
	    if (enderecosDTO == null || enderecosDTO.isEmpty()) {
	        logger.warn("A lista de endere�os est� vazia ou nula.");
	        
	        // Cria uma lista para armazenar os endere�os consultados
	        List<Endereco> enderecos = new ArrayList<>();
	        
	        // Percorre a lista recebida e captura os CEPs
	        for (EnderecoRequestDTO enderecoDTO : enderecosDTO) {
	            // Consulta o endere�o via ViaCEP usando o CEP da lista recebida
	            Endereco endereco = consultarEndereco(enderecoDTO, cliente);
	            // Adiciona o endere�o consultado � lista
	            enderecos.add(endereco);
	        }
	        
	        return enderecos; // Retorna a lista de endere�os consultados
	    }

	    // Mapeia cada EnderecoRequestDTO para um Endereco usando consultarEndereco
	    return enderecosDTO.stream()
	            .map(enderecoDTO -> consultarEndereco(enderecoDTO, cliente))
	            .collect(Collectors.toList());
	}

	// M�todo de consulta ao ViaCEP
	public Endereco consultarEndereco(EnderecoRequestDTO enderecoDTO, Cliente cliente) {
		String cep = enderecoDTO.getCep().replace("-", ""); // Remove o h�fen do CEP se existir
		String url = "https://viacep.com.br/ws/" + cep + "/json/";

		// Realiza a chamada ao ViaCEP
		ViaCepResponse viaCepResponse = restTemplate.getForObject(url, ViaCepResponse.class);

		// Verifica se o retorno � nulo ou cont�m erros
		if (viaCepResponse == null || viaCepResponse.getErro() != null) {
			logger.error("Erro ao consultar o CEP: {}", enderecoDTO.getCep());
			throw new RuntimeException("Erro ao consultar o CEP: " + enderecoDTO.getCep());
		}

		// Cria o endere�o com as informa��es retornadas do ViaCEP
		Endereco endereco = new Endereco();
		endereco.setTipo(enderecoDTO.getTipoEndereco()); // Corrigido para pegar o tipo corretamente
		endereco.setCep(viaCepResponse.getCep()); // Usa o CEP retornado
		endereco.setLogradouro(viaCepResponse.getLogradouro()); // Logradouro retornado
		endereco.setBairro(viaCepResponse.getBairro()); // Bairro retornado
		endereco.setLocalidade(viaCepResponse.getLocalidade()); // Cidade retornada
		endereco.setUf(viaCepResponse.getUf()); // UF retornada
		endereco.setCliente(cliente); // Associa��o com o cliente pai

		return endereco;
	}
}
