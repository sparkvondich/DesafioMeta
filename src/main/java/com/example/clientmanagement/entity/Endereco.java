package com.example.clientmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "enderecos")
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tipo;         
    private String cep;          
    private String logradouro;   
    private String bairro;       
    private String localidade;   
    private String uf;           
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Construtor padrão (opcional, dependendo da sua implementação)
    public Endereco() {
    }

    // Construtor com parâmetros (se você precisar)
    public Endereco(String tipo, String cep, String logradouro, String bairro, String localidade, String uf, Cliente cliente) {
        this.tipo = tipo;
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.cliente = cliente;
    }
}
