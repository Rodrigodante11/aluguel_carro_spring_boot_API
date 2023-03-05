package com.rodrigo.aluguel_carro.service;

import com.rodrigo.aluguel_carro.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente salvar(Cliente cliente);

    Cliente atualizar(Cliente cliente);

    void deletar(Cliente cliente);

    List<Cliente> buscar(Cliente cliente);

    void validar(Cliente cliente);

    Optional<Cliente> obterPorId(Long id);

    List<Cliente> buscarClientesPorNome(String nome);

    void validarEmail(Cliente cliente);
}
