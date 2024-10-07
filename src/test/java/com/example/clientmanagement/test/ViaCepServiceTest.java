package com.example.clientmanagement.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.example.clientmanagement.dto.EnderecoRequestDTO;
import com.example.clientmanagement.entity.Cliente;
import com.example.clientmanagement.entity.Endereco;
import com.example.clientmanagement.integration.ViaCepResponse;
import com.example.clientmanagement.integration.ViaCepService;

class ViaCepServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ViaCepService viaCepService;

    private Cliente cliente;
    private EnderecoRequestDTO enderecoRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializa um cliente de exemplo para ser usado nos testes
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        // Inicializa um EnderecoRequestDTO para testes
        enderecoRequestDTO = new EnderecoRequestDTO();
        enderecoRequestDTO.setCep("01001-000");
        enderecoRequestDTO.setTipoEndereco("Residencial");
    }

    @Test
    void testConsultarEndereco_Sucesso() {
        // Simula a resposta do ViaCep
        ViaCepResponse viaCepResponse = new ViaCepResponse();
        viaCepResponse.setCep("01001-000");
        viaCepResponse.setLogradouro("Pra�a da S�");
        viaCepResponse.setBairro("S�");
        viaCepResponse.setLocalidade("S�o Paulo");
        viaCepResponse.setUf("SP");

        // Configura o mock do RestTemplate
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class))).thenReturn(viaCepResponse);

        // Chama o m�todo para testar
        Endereco endereco = viaCepService.consultarEndereco(enderecoRequestDTO, cliente);

        // Verifica se os campos foram preenchidos corretamente
        assertNotNull(endereco);
        assertEquals("01001-000", endereco.getCep());
        assertEquals("Pra�a da S�", endereco.getLogradouro());
        assertEquals("S�", endereco.getBairro());
        assertEquals("S�o Paulo", endereco.getLocalidade());
        assertEquals("SP", endereco.getUf());
        assertEquals(cliente, endereco.getCliente());
    }

    @Test
    void testConsultarEndereco_Erro() {
        // Simula uma resposta de erro do ViaCep
        ViaCepResponse viaCepResponse = new ViaCepResponse();
        viaCepResponse.setErro("true");

        // Configura o mock do RestTemplate para retornar uma resposta de erro
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class))).thenReturn(viaCepResponse);

        // Verifica se a exce��o � lan�ada corretamente
        Exception exception = assertThrows(RuntimeException.class, () -> {
            viaCepService.consultarEndereco(enderecoRequestDTO, cliente);
        });

        assertEquals("Erro ao consultar o CEP: " + enderecoRequestDTO.getCep(), exception.getMessage());
    }

    @Test
    void testPopularEnderecos_Sucesso() {
        // Cria uma lista de EnderecoRequestDTO para teste
        List<EnderecoRequestDTO> enderecoRequestDTOs = Arrays.asList(enderecoRequestDTO);

        // Simula a resposta do ViaCep
        ViaCepResponse viaCepResponse = new ViaCepResponse();
        viaCepResponse.setCep("01001-000");
        viaCepResponse.setLogradouro("Pra�a da S�");
        viaCepResponse.setBairro("S�");
        viaCepResponse.setLocalidade("S�o Paulo");
        viaCepResponse.setUf("SP");

        // Configura o mock para cada chamada ao ViaCep
        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class))).thenReturn(viaCepResponse);

        // Chama o m�todo para testar
        List<Endereco> enderecos = viaCepService.popularEnderecos(enderecoRequestDTOs, cliente);

        // Verifica se a lista de endere�os foi preenchida corretamente
        assertNotNull(enderecos);
        assertEquals(1, enderecos.size());
        Endereco endereco = enderecos.get(0);
        assertEquals("01001-000", endereco.getCep());
        assertEquals("Pra�a da S�", endereco.getLogradouro());
        assertEquals("S�", endereco.getBairro());
        assertEquals("S�o Paulo", endereco.getLocalidade());
        assertEquals("SP", endereco.getUf());
        assertEquals(cliente, endereco.getCliente());
    }

    @Test
    void testPopularEnderecos_ListaVazia() {
        // Chama o m�todo com lista vazia
        List<Endereco> enderecos = viaCepService.popularEnderecos(Arrays.asList(), cliente);

        // Verifica se a lista retornada est� vazia
        assertNotNull(enderecos);
        assertTrue(enderecos.isEmpty());
    }


}

