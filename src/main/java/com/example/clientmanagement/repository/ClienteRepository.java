package com.example.clientmanagement.repository;

import com.example.clientmanagement.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente pelo CPF (Único)
    Optional<Cliente> findByCpf(String cpf);

    // Buscar clientes pelo nome (Pode haver duplicados)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    List<Cliente> findByEnderecosCep(String cep);
}
