package com.example.clientmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoRequestDTO {

    // Campo para identificar endere�os existentes ao atualizar um cliente
    private Long id;

    @NotBlank(message = "O CEP � obrigat�rio.")
    private String cep;

    @NotBlank(message = "O logradouro � obrigat�rio.")
    private String logradouro;

    @NotBlank(message = "O bairro � obrigat�rio.")
    private String bairro;

    @NotBlank(message = "A cidade � obrigat�ria.")
    private String cidade;

    @NotBlank(message = "O estado � obrigat�rio.")
    private String estado;
    
    @NotBlank(message = "O estado � obrigat�rio.")
    private String tipoEndereco;

    // Campo opcional para complemento (ex: n�mero, apartamento)
    private String complemento;

    @NotBlank(message = "O n�mero � obrigat�rio.")
    private String numero;  // N�mero do endere�o (ex: casa ou apartamento)
}
