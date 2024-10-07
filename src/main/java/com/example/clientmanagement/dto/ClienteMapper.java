package com.example.clientmanagement.dto;

import java.util.stream.Collectors;

import com.example.clientmanagement.entity.Cliente;
import com.example.clientmanagement.entity.Endereco;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());

        // Mapeia a lista de EnderecoRequestDTO para a lista de Enderecos
        if (dto.getEnderecos() != null) {
            cliente.setEnderecos(
                dto.getEnderecos()
                    .stream()
                    .map(enderecoDTO -> {
                        Endereco endereco = new Endereco();
                        endereco.setCep(enderecoDTO.getCep());
                        endereco.setTipo(enderecoDTO.getTipoEndereco()); // Usando tipo de endereço corretamente
                        endereco.setLogradouro(enderecoDTO.getLogradouro());
                        endereco.setBairro(enderecoDTO.getBairro());
                        // As informações de localidade e uf devem ser preenchidas pelo ViaCepService
                        endereco.setCliente(cliente); // Associar o endereço ao cliente
                        return endereco;
                    })
                    .collect(Collectors.toList())
            );
        }

        return cliente;
    }

    public static ClienteResponseDTO toResponseDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());

        // Mapeia a lista de Enderecos para a lista de EnderecoResponseDTOs
        if (cliente.getEnderecos() != null) {
            dto.setEnderecos(
                cliente.getEnderecos()
                    .stream()
                    .map(endereco -> {
                        EnderecoResponseDTO enderecoResponseDTO = new EnderecoResponseDTO();
                        enderecoResponseDTO.setId(endereco.getId());
                        enderecoResponseDTO.setCep(endereco.getCep());
                        enderecoResponseDTO.setLogradouro(endereco.getLogradouro());
                        enderecoResponseDTO.setBairro(endereco.getBairro());
                        // Adicione aqui o mapeamento para localidade e uf se necessário
                        enderecoResponseDTO.setLocalidade(endereco.getLocalidade());
                        enderecoResponseDTO.setUf(endereco.getUf());

                        return enderecoResponseDTO;
                    })
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
