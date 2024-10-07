package com.example.clientmanagement.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotNull(message = "O nome do cliente n�o pode ser nulo.")
    private String nome;

    @NotNull(message = "O CPF do cliente n�o pode ser nulo.")
    private String cpf;

    @NotNull(message = "A lista de endere�os n�o pode ser nula.")
    private List<EnderecoRequestDTO> enderecos = new ArrayList<>(); // Inicializa com lista vazia

   
}
