package com.example.clientmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoRequestDTO {

    // Campo para identificar endereços existentes ao atualizar um cliente
    private Long id;

    @NotBlank(message = "O CEP é obrigatório.")
    private String cep;

    @NotBlank(message = "O logradouro é obrigatório.")
    private String logradouro;

    @NotBlank(message = "O bairro é obrigatório.")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    private String estado;
    
    @NotBlank(message = "O estado é obrigatório.")
    private String tipoEndereco;

    // Campo opcional para complemento (ex: número, apartamento)
    private String complemento;

    @NotBlank(message = "O número é obrigatório.")
    private String numero;  // Número do endereço (ex: casa ou apartamento)
}
