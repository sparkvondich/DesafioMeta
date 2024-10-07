package com.example.clientmanagement.dto;

import lombok.Data;

@Data
public class EnderecoResponseDTO {

    private Long id;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String numero;     
    private String complemento; 
    private String localidade;
    private String uf;
}

