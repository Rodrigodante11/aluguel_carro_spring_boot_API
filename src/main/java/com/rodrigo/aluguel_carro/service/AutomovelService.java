package com.rodrigo.aluguel_carro.service;

import com.rodrigo.aluguel_carro.entity.Automovel;

import java.util.List;
import java.util.Optional;

public interface AutomovelService {

    Automovel salvar(Automovel automovel);

    Automovel atualizar(Automovel automovel);

    void deletar(Automovel automovel);

    List<Automovel> buscar(Automovel automovel);

    void validar(Automovel automovel);

    Optional<Automovel> obterPorId(Long id);

    List<Automovel> obterPorMarca(String marca);

    List<Automovel>  obterPorModelo(String modelo);

    List<Automovel>  obterTodos();

}
