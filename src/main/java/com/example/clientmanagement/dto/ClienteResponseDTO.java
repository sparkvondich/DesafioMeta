package com.example.clientmanagement.dto;

import java.util.List;

import lombok.Data;

@Data
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private List<EnderecoResponseDTO> enderecos;
}

