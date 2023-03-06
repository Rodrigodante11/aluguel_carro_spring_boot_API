package com.rodrigo.aluguel_carro.service;

import com.rodrigo.aluguel_carro.entity.Locacao;

import java.util.List;
import java.util.Optional;

public interface LocacaoService {

    Locacao salvar(Locacao locacao);

    Locacao atualizar(Locacao locacao);

    void deletar(Locacao locacao);

    List<Locacao> buscar(Locacao locacao);

    void validar(Locacao locacao);

    List<Locacao> buscarTodosPorClienteId(Long id);

    List<Locacao> buscarTodosPorAutomovelId(Long id);

    Optional<Locacao> obterPorId(Long id);

    List<Locacao>  obterTodos();
}
