package com.example.clientmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.clientmanagement.dto.ClienteMapper;
import com.example.clientmanagement.dto.ClienteRequestDTO;
import com.example.clientmanagement.dto.ClienteResponseDTO;
import com.example.clientmanagement.dto.EnderecoRequestDTO;
import com.example.clientmanagement.entity.Cliente;
import com.example.clientmanagement.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        Cliente clienteSalvo = clienteService.cadastrarCliente(clienteRequestDTO);
        return ResponseEntity.ok(clienteSalvo);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obterCliente(@PathVariable Long id) {
        return clienteService.obterCliente(id)
                .map(cliente -> ResponseEntity.ok(ClienteMapper.toResponseDTO(cliente)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody ClienteRequestDTO clienteRequestDTO) {
        // Extrair endereços do ClienteRequestDTO
        List<EnderecoRequestDTO> enderecosDTO = clienteRequestDTO.getEnderecos();

        // Atualiza o cliente usando o serviço
        Optional<Cliente> clienteAtualizado = Optional.ofNullable(clienteService.atualizarCliente(id, enderecosDTO));
        
        // Retorna a resposta apropriada
        return clienteAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        clienteService.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }
}
