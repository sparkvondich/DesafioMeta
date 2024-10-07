package com.example.clientmanagement.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ViaCepResponse {
    
    @JsonProperty("cep")
    private String cep;

    @JsonProperty("logradouro")
    private String logradouro;

    @JsonProperty("bairro")
    private String bairro;

    @JsonProperty("localidade")
    private String localidade;

    @JsonProperty("uf")
    private String uf;

    @JsonProperty("erro")
    private String erro;
}
