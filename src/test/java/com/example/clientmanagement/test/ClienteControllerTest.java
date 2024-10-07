package com.example.clientmanagement.test;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.clientmanagement.controller.ClienteController;
import com.example.clientmanagement.dto.ClienteMapper;
import com.example.clientmanagement.dto.ClienteRequestDTO;
import com.example.clientmanagement.dto.ClienteResponseDTO;
import com.example.clientmanagement.entity.Cliente;
import com.example.clientmanagement.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private MockMvc mockMvc;
    private Cliente cliente;
    private ClienteRequestDTO clienteRequestDTO;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();

        // Inicializa dados de teste
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setNome("Cliente Teste");

        clienteResponseDTO = ClienteMapper.toResponseDTO(cliente);
    }

    @Test
    void testCriarCliente() throws Exception {
        // Configurar mocks
        when(clienteService.cadastrarCliente(any(ClienteRequestDTO.class))).thenReturn(cliente);

        // Executar e verificar a chamada POST
        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()))
                .andExpect(jsonPath("$.nome").value(cliente.getNome()));

        verify(clienteService, times(1)).cadastrarCliente(any(ClienteRequestDTO.class));
    }

    @Test
    void testObterCliente_Encontrado() throws Exception {
        // Configurar mocks para cliente encontrado
        when(clienteService.obterCliente(cliente.getId())).thenReturn(Optional.of(cliente));

        // Executar e verificar a chamada GET
        mockMvc.perform(get("/clientes/{id}", cliente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clienteResponseDTO.getId()))
                .andExpect(jsonPath("$.nome").value(clienteResponseDTO.getNome()));

        verify(clienteService, times(1)).obterCliente(cliente.getId());
    }

    @Test
    void testObterCliente_NaoEncontrado() throws Exception {
        // Configurar mocks para cliente não encontrado
        when(clienteService.obterCliente(cliente.getId())).thenReturn(Optional.empty());

        // Executar e verificar a chamada GET
        mockMvc.perform(get("/clientes/{id}", cliente.getId()))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).obterCliente(cliente.getId());
    }

    @Test
    void testAtualizarCliente() throws Exception {
        // Configurar mocks
        when(clienteService.atualizarCliente(anyLong(), any())).thenReturn(cliente);

        // Executar e verificar a chamada PUT
        mockMvc.perform(put("/clientes/{id}", cliente.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cliente.getId()))
                .andExpect(jsonPath("$.nome").value(cliente.getNome()));

        verify(clienteService, times(1)).atualizarCliente(anyLong(), any());
    }

    @Test
    void testAtualizarCliente_NaoEncontrado() throws Exception {
        // Configurar mocks para cliente não encontrado
        when(clienteService.atualizarCliente(anyLong(), any())).thenReturn(null);

        // Executar e verificar a chamada PUT
        mockMvc.perform(put("/clientes/{id}", cliente.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteRequestDTO)))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).atualizarCliente(anyLong(), any());
    }

    @Test
    void testExcluirCliente() throws Exception {
        // Executar e verificar a chamada DELETE
        mockMvc.perform(delete("/clientes/{id}", cliente.getId()))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).excluirCliente(cliente.getId());
    }

    // Converte um objeto para JSON (usado para enviar requisições)
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

