package com.example.clientmanagement.test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.clientmanagement.dto.ClienteRequestDTO;
import com.example.clientmanagement.dto.EnderecoRequestDTO;
import com.example.clientmanagement.entity.Cliente;
import com.example.clientmanagement.entity.Endereco;
import com.example.clientmanagement.exception.ResourceNotFoundException;
import com.example.clientmanagement.integration.ViaCepService;
import com.example.clientmanagement.repository.ClienteRepository;
import com.example.clientmanagement.service.ClienteService;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ViaCepService viaCepService;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteRequestDTO clienteRequestDTO;
    private List<EnderecoRequestDTO> enderecosDTO;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializa dados de teste
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setLogradouro("Rua A");

        EnderecoRequestDTO enderecoRequestDTO = new EnderecoRequestDTO();
        enderecoRequestDTO.setCep("12345678");
        enderecosDTO = Arrays.asList(enderecoRequestDTO);

        clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setNome("Cliente Teste");
        clienteRequestDTO.setEnderecos(enderecosDTO);
    }

    @Test
    void testCadastrarCliente() {
        // Configurar mocks
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(viaCepService.popularEnderecos(anyList(), any(Cliente.class))).thenReturn(Arrays.asList(endereco));

        // Executar o método
        Cliente clienteCadastrado = clienteService.cadastrarCliente(clienteRequestDTO);

        // Verificar resultados
        assertEquals(cliente.getNome(), clienteCadastrado.getNome());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(viaCepService, times(1)).popularEnderecos(anyList(), any(Cliente.class));
    }

    @Test
    void testAtualizarCliente() {
        // Configurar mocks
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(viaCepService.consultarEndereco(any(EnderecoRequestDTO.class), any(Cliente.class))).thenReturn(endereco);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Executar o método
        Cliente clienteAtualizado = clienteService.atualizarCliente(cliente.getId(), enderecosDTO);

        // Verificar resultados
        assertEquals(cliente.getId(), clienteAtualizado.getId());
        assertEquals(1, clienteAtualizado.getEnderecos().size());
        verify(clienteRepository, times(1)).findById(cliente.getId());
        verify(viaCepService, times(1)).consultarEndereco(any(EnderecoRequestDTO.class), any(Cliente.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testAtualizarCliente_ClienteNaoEncontrado() {
        // Configurar mocks para cliente não encontrado
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());

        // Verificar se lança a exceção
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.atualizarCliente(cliente.getId(), enderecosDTO);
        });

        verify(clienteRepository, times(1)).findById(cliente.getId());
        verify(viaCepService, times(0)).consultarEndereco(any(EnderecoRequestDTO.class), any(Cliente.class));
    }

    @Test
    void testObterCliente() {
        // Configurar mocks
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));

        // Executar o método
        Optional<Cliente> clienteEncontrado = clienteService.obterCliente(cliente.getId());

        // Verificar resultados
        assertEquals(cliente.getNome(), clienteEncontrado.get().getNome());
        verify(clienteRepository, times(1)).findById(cliente.getId());
    }

    @Test
    void testObterCliente_NaoEncontrado() {
        // Configurar mocks para cliente não encontrado
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.empty());

        // Executar o método
        Optional<Cliente> clienteEncontrado = clienteService.obterCliente(cliente.getId());

        // Verificar que não encontrou o cliente
        assertEquals(Optional.empty(), clienteEncontrado);
        verify(clienteRepository, times(1)).findById(cliente.getId());
    }

    @Test
    void testExcluirCliente() {
        // Executar o método
        clienteService.excluirCliente(cliente.getId());

        // Verificar que o método de exclusão foi chamado
        verify(clienteRepository, times(1)).deleteById(cliente.getId());
    }
}

