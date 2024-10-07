package com.example.clientmanagement.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.clientmanagement.entity.Cliente;
import com.example.clientmanagement.entity.Endereco;

class EnderecoTest {

    private Endereco endereco;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Inicializa um objeto Cliente para testes
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");

        // Inicializa um Endereco com dados de teste
        endereco = new Endereco("Residencial", "01001-000", "Pra�a da S�", "S�", "S�o Paulo", "SP", cliente);
    }

    @Test
    void testConstrutorComParametros() {
        // Verifica se os campos foram corretamente atribu�dos
        assertEquals("Residencial", endereco.getTipo());
        assertEquals("01001-000", endereco.getCep());
        assertEquals("Pra�a da S�", endereco.getLogradouro());
        assertEquals("S�", endereco.getBairro());
        assertEquals("S�o Paulo", endereco.getLocalidade());
        assertEquals("SP", endereco.getUf());
        assertEquals(cliente, endereco.getCliente());
    }

    @Test
    void testGettersAndSetters() {
        // Testa getters e setters para cada atributo
        endereco.setTipo("Comercial");
        assertEquals("Comercial", endereco.getTipo());

        endereco.setCep("02002-000");
        assertEquals("02002-000", endereco.getCep());

        endereco.setLogradouro("Rua da Consola��o");
        assertEquals("Rua da Consola��o", endereco.getLogradouro());

        endereco.setBairro("Consola��o");
        assertEquals("Consola��o", endereco.getBairro());

        endereco.setLocalidade("S�o Paulo");
        assertEquals("S�o Paulo", endereco.getLocalidade());

        endereco.setUf("RJ");
        assertEquals("RJ", endereco.getUf());

        Cliente novoCliente = new Cliente();
        novoCliente.setId(2L);
        novoCliente.setNome("Novo Cliente");
        endereco.setCliente(novoCliente);
        assertEquals(novoCliente, endereco.getCliente());
    }

    @Test
    void testEqualsAndHashCode() {
        // Testa se dois objetos iguais t�m o mesmo hashCode e s�o iguais
        Endereco endereco2 = new Endereco("Residencial", "01001-000", "Pra�a da S�", "S�", "S�o Paulo", "SP", cliente);

        assertEquals(endereco, endereco2);
        assertEquals(endereco.hashCode(), endereco2.hashCode());
    }

    @Test
    void testNotEqualsAndHashCode() {
        // Testa se objetos diferentes n�o s�o iguais e t�m hashCodes diferentes
        Endereco endereco2 = new Endereco("Comercial", "02002-000", "Rua da Consola��o", "Consola��o", "S�o Paulo", "RJ", cliente);

        assertNotEquals(endereco, endereco2);
        assertNotEquals(endereco.hashCode(), endereco2.hashCode());
    }



    @Test
    void testConstrutorSemParametros() {
        // Testa o construtor padr�o e verifica se os campos est�o nulos por padr�o
        Endereco enderecoVazio = new Endereco();

        assertNull(enderecoVazio.getTipo());
        assertNull(enderecoVazio.getCep());
        assertNull(enderecoVazio.getLogradouro());
        assertNull(enderecoVazio.getBairro());
        assertNull(enderecoVazio.getLocalidade());
        assertNull(enderecoVazio.getUf());
        assertNull(enderecoVazio.getCliente());
    }
}

